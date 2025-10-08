package myuniquesite.blerp.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final Long resourceId;

    public ResourceNotFoundException(String resourceName, Long resourceId) {
        super(resourceName + " with ID " + resourceId + " not found.");
        this.resourceId = resourceId;
    }



    public Long getResourceId() {
        return resourceId;
    }
}

