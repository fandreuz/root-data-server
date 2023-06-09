package io.github.fandreuz.open.data.server.conversion;

import java.nio.file.Path;

import lombok.NonNull;

/**
 * Interface for services providing conversion from supported dataset types to
 * CSV.
 *
 * @author fandreuz
 */
public interface ConversionService {

   /**
    * Convert the source file to CSV.
    *
    * @param source
    *            original file.
    * @return path to the converted file if available.
    */
   Path convert(@NonNull Path source);
}
