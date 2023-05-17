package com.bassemHalim.cyclopath.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Compressor {

    public static byte[] compress(byte[] data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream out = new GZIPOutputStream(bos);
            out.write(data);
            out.close();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // a function to decompress data zipped using gzip

    /**
     * decompress gzip compressed data and returns the decompressed data or null in case of failure
     *
     * @param compressedData the byte[] to decompress
     * @return the decompressed byte[] or null if decompression failed
     */
    public static byte[] decompress(byte[] compressedData) {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            gzipInputStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }
}
