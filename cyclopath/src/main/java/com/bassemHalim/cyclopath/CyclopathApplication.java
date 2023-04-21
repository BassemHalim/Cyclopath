package com.bassemHalim.cyclopath;

import com.bassemHalim.cyclopath.FIT.FITDownloader;
import com.bassemHalim.cyclopath.FIT.FITGarminDownloader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CyclopathApplication {

    public static void main(String[] args) {
        SpringApplication.run(CyclopathApplication.class, args);
        FITDownloader downloader = new FITGarminDownloader("", "");
    }

}
