package com.iluwa.accountmanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferResponse {

    private AccountDto from;

    private AccountDto to;
}
