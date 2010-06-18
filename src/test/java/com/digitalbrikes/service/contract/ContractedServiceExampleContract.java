package com.digitalbrikes.service.contract;

import com.digitalbrikes.service.ContractedServiceExample;

public final class ContractedServiceExampleContract {
    public boolean preDoSomething(final ContractedServiceExample service, final Object o) {
        return null != o;
    }

    public boolean postDoSomething(final ContractedServiceExample service, final Object o, final Object result) {
        return (o == result);
    }
}
