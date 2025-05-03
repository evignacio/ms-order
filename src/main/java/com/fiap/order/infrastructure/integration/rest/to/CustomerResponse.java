package com.fiap.order.infrastructure.integration.rest.to;

import java.util.Set;

public record CustomerResponse(String id, Set<AddressTO> addresses) {
}
