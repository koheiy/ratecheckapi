package co.jp.ratecheckapi.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class RateCheckController {

    @Value("${file.saveDirectory}")
    private String directory;

    @Value("${file.filenamebase}")
    private String fileNameBase;

    @GetMapping("/mizuho/rate/{targetDate}")
    public ResponseEntity<String> getRate(@PathVariable("targetDate") String targetDate) {

        if (Strings.isEmpty(targetDate)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String targetFile = directory + fileNameBase.replace("yyyyMMdd", targetDate);

        try {
            JsonNode root = new ObjectMapper().readTree(new File(targetFile));
            return new ResponseEntity<>(root.toString(), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
