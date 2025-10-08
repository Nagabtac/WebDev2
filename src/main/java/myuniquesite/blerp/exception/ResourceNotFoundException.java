package myuniquesite.blerp.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final Integer resourceId;

    public ResourceNotFoundException(String message, Integer resourceId) {
        super(message);
        this.resourceId = resourceId;
    }

    public Integer getResourceId() {
        return resourceId;
    }
}