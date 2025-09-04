package myuniquesite.blerp.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String resource, int id) {
        super(resource + " with ID " + id + " not found.");
    }
}
