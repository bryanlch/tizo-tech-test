package com.digitaltech.sim.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

/**
 * Wrapper class to standardize all API responses.
 * Ensures that clients consume a predictable and uniform JSON format.
 * * @param <T> The type of the payload or data contained in the response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * Internal HTTP status used to build the final Spring response.
     * Ignored in JSON serialization to avoid redundancy in the response body.
     */
    @JsonIgnore
    private HttpStatus httpStatus;

    /**
     * HTTP status code represented as an integer (e.g., 200, 400, 500).
     */
    private Integer code;

    /**
     * Descriptive message about the operation's result.
     * It can be a success message or error details.
     */
    private String message;

    /**
     * The actual data returned by the operation.
     * It will be null and omitted from the JSON in case of errors where there is no payload.
     */
    private T data;

    /**
     * Constructor for responses that only require an HTTP status.
     * Uses the default reason phrase of the status (e.g., "Not Found" for 404).
     * * @param httpStatus The HTTP status of the response.
     */
    public ApiResponse(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.code = this.httpStatus.value();
        this.message = this.httpStatus.getReasonPhrase();
    }

    /**
     * Constructor for responses with an HTTP status and a custom message.
     * * @param httpStatus The HTTP status of the response.
     * @param message    The custom message to return.
     */
    public ApiResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.code = this.httpStatus.value();
        this.message = message; // Bug fixed: previously ignored the message parameter
    }

    /**
     * Creates a successful response (200 OK) with a data payload and a custom message.
     * * @param data    The data to return to the client.
     * @param message A descriptive message of the successful operation.
     * @param <T>     The data type of the payload.
     * @return A configured successful ApiResponse instance.
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(OK, OK.value(), message, data);
    }

    /**
     * Creates a successful response (200 OK) with a data payload and the default message ("OK").
     * * @param data The data to return to the client.
     * @param <T>  The data type of the payload.
     * @return A configured successful ApiResponse instance.
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(OK, OK.value(), OK.getReasonPhrase(), data);
    }

    /**
     * Creates a generic server error response (500 Internal Server Error).
     * * @param message The error detail or message.
     * @param <T>     The expected data type (usually inferred as generic or Void).
     * @return A configured server error ApiResponse instance.
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.value(), message, null);
    }

    /**
     * Creates a client bad request error response (400 Bad Request).
     * * @param message The message explaining why the request is invalid.
     * @param <T>     The expected data type (usually inferred as generic or Void).
     * @return A configured bad request ApiResponse instance.
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), message, null);
    }

    /**
     * Converts this ApiResponse instance into a standard Spring Boot ResponseEntity.
     * This couples the standardized body with the actual HTTP headers and status code.
     * * @return A ResponseEntity ready to be returned by a controller.
     */
    public ResponseEntity<ApiResponse<T>> toHttp() {
        return ResponseEntity.status(this.httpStatus).body(this);
    }
}