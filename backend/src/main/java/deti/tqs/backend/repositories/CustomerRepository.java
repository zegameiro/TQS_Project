package deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import deti.tqs.backend.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
  
}
