package com.progettoTAASS.user.repository;

import com.progettoTAASS.user.model.Wallet;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet, Integer> {
    Wallet findWalletById(int id);
}
