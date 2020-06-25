package it.logistics.common.domain.model;

import java.util.ArrayList;
import java.util.List;

public class DomainEventPublisher {

    private static final ThreadLocal<DomainEventPublisher> instance = new ThreadLocal<DomainEventPublisher>() {
        protected DomainEventPublisher initialValue() {
            return new DomainEventPublisher();
        }
    };

    private boolean publishing;

    @SuppressWarnings("rawtypes")
    private List subscribers;

    public static DomainEventPublisher getInstance() {
        return instance.get();
    }

    public <T> void publish(final T aDomainEvent) {
        if (!this.isPublishing() && this.hasSubscribers()) {

            try {
                this.setPublishing(true);

                Class<?> eventType = aDomainEvent.getClass();

                @SuppressWarnings("unchecked")
                List<DomainEventSubscriber<T>> allSubscribers = this.getSubscribers();

                for (DomainEventSubscriber<T> subscriber : allSubscribers) {
                    Class<?> subscribedToType = subscriber.subscribedToEventType();

                    if (eventType == subscribedToType || subscribedToType == DomainEvent.class) {
                        subscriber.handleEvent(aDomainEvent);
                    }
                }

            } finally {
                this.setPublishing(false);
            }
        }
    }

    public void reset() {
        if (!this.isPublishing()) {
            this.setSubscribers(null);
        }
    }

    @SuppressWarnings("rawtypes")
    private void resetSubscribersList() {
        if (!this.hasSubscribers()) {
            this.setSubscribers(new ArrayList());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void subscribe(DomainEventSubscriber<T> aSubscriber) {
        if (!this.isPublishing()) {
            this.resetSubscribersList();

            this.getSubscribers().add(aSubscriber);
        }
    }

    private DomainEventPublisher() {
        super();

        this.setPublishing(false);
        this.resetSubscribersList();
    }

    private boolean isPublishing() {
        return this.publishing;
    }

    private void setPublishing(boolean flag) {
        this.publishing = flag;
    }

    private boolean hasSubscribers() {
        return this.getSubscribers() != null;
    }

    @SuppressWarnings("rawtypes")
    private List getSubscribers() {
        return this.subscribers;
    }

    @SuppressWarnings("rawtypes")
    private void setSubscribers(List subscribersList) {
        this.subscribers = subscribersList;
    }

}
