package it.logistics.tracking.resource;

import it.logistics.tracking.application.ParcelService;
import it.logistics.tracking.domain.model.employee.Employee;
import it.logistics.tracking.domain.model.parcel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.HttpStatus.OK;


@CrossOrigin(maxAge = 3600)
@RequestMapping("/tracking")
@RestController
public class ParcelResource {

    private final ParcelService parcelService;

    private final DeliveryWorkerService deliveryWorkerService;

    @Autowired
    public ParcelResource(ParcelService parcelService, DeliveryWorkerService deliveryWorkerService) {
        this.parcelService = parcelService;
        this.deliveryWorkerService = deliveryWorkerService;
    }

    @PreAuthorize("hasAnyAuthority('COURIER', 'ADMINISTRATOR')")
    @PostMapping(path = "/parcel")
    public ResponseEntity<Parcel> addParcel(@RequestBody Parcel parcel, HttpServletRequest request) {
        final String username = request.getUserPrincipal().getName();
        parcel.setModified_by(username);
        DeliveryWorker creator = null;
        try {
            creator = deliveryWorkerService.administratorFrom(new Employee(username));
        } catch (IllegalStateException e) { }
        try {
            creator = deliveryWorkerService.courierFrom(new Employee(username));
        } catch (IllegalStateException e) { }
        if (creator == null) {
            throw new IllegalArgumentException("You should exist in HR DB.");
        }
        try {
            String courierId = null;
            if (parcel.getCourier() != null) {
                courierId = parcel.getCourier().getId();
            } else {
                parcelService.addParcel(parcel);
                return new ResponseEntity<>(parcel, HttpStatus.OK);
            }
            Courier courier = deliveryWorkerService.courierFrom(new Employee(courierId));
            if (courier != null && creator != null) {
                Parcel newParcel = new Parcel(parcel.getSender(), parcel.getReceiver(), courier, creator);
                parcelService.addParcel(newParcel);
                return new ResponseEntity<>(parcel, HttpStatus.OK);
            }
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Courier should exist in HR DB.");
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PreAuthorize("hasAnyAuthority('COURIER', 'SORTING', 'ADMINISTRATOR')")
    @RequestMapping(method = RequestMethod.GET, path = "/parcel")
    @ResponseBody
    ResponseEntity<Parcel> getParcels(
            @RequestParam(name = "sort", required = false, defaultValue = "[\"id\",\"ASC\"]") String sort,
            @RequestParam(name = "range", required = false, defaultValue = "[0,9]") String range,
            @RequestParam(name = "filter", required = false, defaultValue = "{}") String filter,
            HttpServletRequest request) {
        String[] sortArr = sort.replace ("[", "").replace ("]", "").replaceAll("\"", "").split(",");
        String[] rangeArr = range.replace ("[", "").replace ("]", "").replaceAll("\"", "").split(",");
        String filterExp = extractFullSearch(filter);
        Boolean filterAvailableOnly = extractAvailable(filter);
        final int start = Integer.parseInt(rangeArr[0]);
        final int end = Integer.parseInt(rangeArr[1]);
        final int size = end - start + 1;
        final int page = start / size;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(sortArr[1]), sortArr[0]);
        Page<Parcel> result = null;
        if (filterExp.equals("") && !filterAvailableOnly) {
            result = parcelService.allParcels(pageRequest);
        } else {
            final String username = request.getUserPrincipal().getName();
            DeliveryWorker creator = null;
            try {
                creator = deliveryWorkerService.administratorFrom(new Employee(username));
                if (creator != null) {
                    result = parcelService.allParcels(pageRequest,
                            filterExp,
                            filterAvailableOnly,
                            creator);
                }
            } catch (IllegalStateException e) { }
            try {
                creator = deliveryWorkerService.courierFrom(new Employee(username));
                if (result == null && creator != null) {
                    result = parcelService.allParcels(pageRequest,
                            filterExp,
                            filterAvailableOnly,
                            creator);
                }
            } catch (IllegalStateException e) { }
            try {
                creator = deliveryWorkerService.sortingWorkerFrom(new Employee(username));
                if (result == null && creator != null) {
                    result = parcelService.allParcels(pageRequest,
                            filterExp,
                            filterAvailableOnly,
                            creator);
                }
            } catch (IllegalStateException e) { }
            if (result == null && creator == null) {
                throw new IllegalArgumentException("You should exist in HR DB.");
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", "parcel" + " " + start + " - " + end + " / " + result.getTotalElements());
        headers.add("Access-Control-Expose-Headers", "Content-Range");
        return new ResponseEntity(result.getContent(), headers, OK);
    }

    private Boolean extractAvailable(String filter) {
        final String filterExp = filter.replace("{", "").replace("}", "");
        if (filterExp.equals("")) {
            return false;
        }
        Matcher matcher = Pattern.compile("\"available\":[^\"]+").matcher(filterExp);
        String queryExp = null;
        if (matcher.find()) {
            queryExp = matcher.group();
        }
        if (queryExp == null) {
            return false;
        }
        String value = queryExp.replace("\"available\":", "").replace("\"", "");
        return value.equals("true");
    }

    private String extractFullSearch(String filter) {
        final String filterExp = filter.replace("{", "").replace("}", "");
        if (filterExp.equals("")) {
            return "";
        }
        Matcher matcher = Pattern.compile("\"q\":\"[^\"]+\"").matcher(filterExp);
        String queryExp = null;
        if (matcher.find()) {
            queryExp = matcher.group();
        }
        if (queryExp == null) {
            return "";
        }
        String query = null;
        query = queryExp.replace("\"q\":", "").replace("\"", "");
        return query;
    }

    @PreAuthorize("hasAnyAuthority('COURIER', 'SORTING', 'ADMINISTRATOR')")
    @GetMapping(path = "/parcel/{id}")
    public Parcel getParcel(@PathVariable Long id) {
        Parcel parcel = parcelService.findParcel(id);
        return parcel;
    }

    @PreAuthorize("hasAnyAuthority('COURIER', 'ADMINISTRATOR')")
    @PatchMapping(path = "/parcel/courier/received/{parcelId}")
    public void courierReceived(@PathVariable Long parcelId, HttpServletRequest request) {
        final String username = request.getUserPrincipal().getName();
        Courier courier = null;
        try {
            courier = deliveryWorkerService.courierFrom(new Employee(username));
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Courier should exist in HR DB.");
        }
        try {
            if (courier != null) {
                parcelService.courierReceived(courier, parcelId);
            }
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Parcel " + parcelId + " must be in 'created', 'destination sorting' or 'return source sorting' state.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parcel not exists");
        }
    }

    @PreAuthorize("hasAnyAuthority('SORTING', 'ADMINISTRATOR')")
    @PatchMapping(path = "/parcel/sorting/received/{parcelId}")
    public void sortingReceived(@PathVariable Long parcelId, HttpServletRequest request) {
        final String username = request.getUserPrincipal().getName();
        SortingWorker sortingWorker = null;
        try {
            sortingWorker = deliveryWorkerService.sortingWorkerFrom(new Employee(username));
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Courier should exist in HR DB.");
        }
        try {
            if (sortingWorker != null) {
                parcelService.sortingReceived(sortingWorker, parcelId);
            }
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Parcel " + parcelId + " 'pickup', 'transit' or it's return equivalent state.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parcel not exists");
        }
    }

    @PreAuthorize("hasAnyAuthority('SORTING', 'ADMINISTRATOR')")
    @PatchMapping(path = "/parcel/sorting/left/{parcelId}")
    public void sortingLeft(@PathVariable Long parcelId, HttpServletRequest request) {
        final String username = request.getUserPrincipal().getName();
        SortingWorker sortingWorker = null;
        try {
            sortingWorker = deliveryWorkerService.sortingWorkerFrom(new Employee(username));
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Courier should exist in HR DB.");
        }
        try {
            if (sortingWorker != null) {
                parcelService.sortingLeft(sortingWorker, parcelId);
            }
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Parcel " + parcelId + " 'source sorting' or 'return destination sorting' state.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parcel not exists");
        }
    }

    @PreAuthorize("hasAnyAuthority('COURIER', 'ADMINISTRATOR')")
    @PatchMapping(path = "/parcel/courier/delivered/{parcelId}")
    public void courierDelivered(@PathVariable Long parcelId, HttpServletRequest request) {
        final String username = request.getUserPrincipal().getName();
        Courier courier = null;
        try {
            courier = deliveryWorkerService.courierFrom(new Employee(username));
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Courier should exist in HR DB.");
        }
        try {
            if (courier != null) {
                parcelService.courierDelivered(courier, parcelId);
            }
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Parcel " + parcelId + " must be in 'in delivery' or in it's return equivalent state.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parcel not exists");
        }
    }

    @PreAuthorize("hasAnyAuthority('COURIER', 'ADMINISTRATOR')")
    @PatchMapping(path = "/parcel/courier/returned/{parcelId}")
    public void courierReturned(@PathVariable Long parcelId, HttpServletRequest request) {
        final String username = request.getUserPrincipal().getName();
        Courier courier = null;
        try {
            courier = deliveryWorkerService.courierFrom(new Employee(username));
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Courier should exist in HR DB.");
        }
        try {
            if (courier != null) {
                parcelService.courierReturned(courier, parcelId);
            }
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Parcel " + parcelId + " must be in 'in delivery' or it's equivalent state.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Parcel not exists");
        }
    }

}
