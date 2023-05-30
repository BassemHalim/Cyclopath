package com.bassemHalim.cyclopath.Expections;

public record APIError(
        String path,
        String msg,
        int error_code,
        String timestamp

) {
}
