package com.arbitrage.common.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenReissue {
    String grant_type;
    String client_id;
    String redirect_url;
    String code;

}
