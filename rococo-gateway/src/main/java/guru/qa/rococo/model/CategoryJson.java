package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CategoryJson(
    @JsonProperty("id")
    UUID id,
    @NotBlank(message = "Category can not be blank")
    @Size(min = 2, max = 50, message = "Allowed category length should be from 2 to 50 characters")
    @JsonProperty("name")
    String name,
    @JsonProperty("username")
    String username,
    @JsonProperty("archived")
    boolean archived) {
}
