package ru.bkmz1994.http.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentDescription {
    @JsonProperty("participantInn")
    private String participantInn;
}
