// ResourceNotFoundException.java
package com.zjgsu.ms.hxy.CampusCourseSelectionSystem.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}