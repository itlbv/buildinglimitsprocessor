package com.itlbv.buildinglimitsprocessor

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/geodata")
class GeoDataController {
    @PostMapping
    fun geoData(@RequestBody body: String): ResponseEntity<Any> {
        return ok("emptyBody")
    }
}