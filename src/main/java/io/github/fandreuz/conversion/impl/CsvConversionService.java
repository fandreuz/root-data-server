package io.github.fandreuz.conversion.impl;

import io.github.fandreuz.conversion.ConversionService;
import java.nio.file.Path;

/**
 * Implementation of {@link ConversionService} for CSV files.
 *
 * @author fandreuz
 */
public class CsvConversionService implements ConversionService {

    @Override
    public Path convert(Path source) {
        // Path is not immutable
        return Path.of(source.toUri());
    }
}