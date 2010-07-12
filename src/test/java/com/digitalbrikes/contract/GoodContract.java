package com.digitalbrikes.contract;

import com.digitalbrikes.contract.SimpleContractTest.ContractService;

public final class GoodContract {
    public boolean precondition(final ContractService service) {
        return true;
    }

    public boolean postcondition(final ContractService service) {
        return true;
    }
}
