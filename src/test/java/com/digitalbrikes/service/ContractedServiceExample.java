package com.digitalbrikes.service;

import com.digitalbrikes.contract.Contracted;
import com.digitalbrikes.contract.PostConditioned;
import com.digitalbrikes.contract.PreConditioned;

@Contracted
public interface ContractedServiceExample {
    @PreConditioned
    @PostConditioned
    Object doSomething(Object o);

    Object nonContractedAction(Object o);

    @PreConditioned
    Object missingPreconditionAction(Object o);

    @PostConditioned
    Object missingPostconditionAction(Object o);
}
