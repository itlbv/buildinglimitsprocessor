package com.itlbv.buildinglimitsprocessor

import com.itlbv.buildinglimitsprocessor.service.GeoDataService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/geodata")
class GeoDataController(
    private val geoDataService: GeoDataService,
) {
    @PostMapping
    fun geoData(@RequestBody geoData: String): ResponseEntity<Any> {
        geoDataService.parseAndSaveGeoData(geoData)
        return ok("success")
    }
}