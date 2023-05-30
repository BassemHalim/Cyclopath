package com.bassemHalim.cyclopath.Map;


import com.bassemHalim.cyclopath.Utils.Compressor;
import jakarta.validation.constraints.Positive;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activity")
public class MapController {
    @Autowired
    private MapService mapService;

    @GetMapping("/{id}/map")
    public ResponseEntity<Void> getMap(@PathVariable @Positive Long id) {
        HttpUrl url = mapService.getMap(id).orElseThrow();
        return ResponseEntity.status(HttpStatus.FOUND).location(url.uri()).build();
    }

    @GetMapping("{id}/route")
    ResponseEntity<RouteDTO> getRoute(@PathVariable Long id) {
        Route route = mapService.getRoute(id);
        RouteDTO dto = RouteMapper.MAPPER.toDTO(route);
        String json = new String(Compressor.decompress(dto.geoJSON_zip));
        System.out.println(json);
        return ResponseEntity.ok(dto);
    }


}
