package io.github.fandreuz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum listing all supported dataset types.
 *
 * @author fandreuz
 */
@AllArgsConstructor
@Getter
public enum DatasetType {
    ROOT("root"),
    CSV("csv"),
    JSON("json");

    private final String extension;
}
