package com.simo333.beauty_manager_service.security.payload.freebusy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class FreeBusyResponse {
    private final boolean busy;
    private final String nextFreeDate;
}
