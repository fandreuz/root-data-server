package io.github.fandreuz.open.data.server.fetch;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to read the content of remote files.
 * <p>
 * This class is not safe with respect to concurrent download of files with the
 * same name. Users should make sure this does not happen.
 *
 * @author fandreuz
 */
@Slf4j
final class DownloadUtils {

   /**
    * Try to read the file at {@code fileUrl}.
    *
    * @param fileUrl
    *            url to the file to be read.
    * @return the file content if available.
    */
   static Path download(@NonNull String fileUrl) {
      log.info("Downloading URL '{}'", fileUrl);
      URL url;
      try {
         url = new URL(fileUrl);
      } catch (Exception exception) {
         throw new FetchException("An error occurred while parsing the URL", exception);
      }

      Path localFile = Path.of(extractFileName(fileUrl));
      if (!Files.exists(localFile)) {
         try {
            Files.createFile(localFile);
         } catch (Exception exception) {
            String msg = "An error occurred while creating the file: " + localFile.toAbsolutePath();
            throw new FetchException(msg, exception);
         }
      }

      log.info("Target file name for '{}': '{}'", fileUrl, localFile);
      try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(localFile.toFile());
            FileChannel fileChannel = fileOutputStream.getChannel() //
      ) {
         fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
      } catch (Exception exception) {
         try {
            Files.deleteIfExists(localFile);
            log.info("The file '{}' was removed", localFile);
         } catch (Exception deleteException) {
            log.warn("Could not delete the partially downloaded file '{}' (URL='{}')", localFile, fileUrl);
         }

         String msg = String.format("An error occurred while reading the file at '%s'", fileUrl);
         throw new FetchException(msg, exception);
      }

      log.info("Download completed: '{}'", fileUrl);
      return localFile;
   }

   // TODO It's not necessarily true that the URL contains a proper file name
   private static String extractFileName(@NonNull String fileUrl) {
      int lastSlashIndex = fileUrl.lastIndexOf('/');
      return fileUrl.substring(lastSlashIndex + 1);
   }
}
