package vendas2.modelo.dto;

import java.time.LocalDate;

public class ClienteCreateErrorResponseDTO {
    private Integer id;
    private String message;
    private LocalDate createdAt;
    private String requestUrl;

    public ClienteCreateErrorResponseDTO(
            Integer id,
            String message,
            LocalDate createdAt,
            String requestUrl) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.requestUrl = requestUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
}
