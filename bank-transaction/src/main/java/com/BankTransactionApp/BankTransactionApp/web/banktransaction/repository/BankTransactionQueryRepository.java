package com.BankTransactionApp.BankTransactionApp.web.banktransaction.repository;

import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.ResponseDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.util.TransactionType;
import com.BankTransactionApp.BankTransactionApp.web.bank.domain.QBank;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.domain.QBankTransaction;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class BankTransactionQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    @Value("${flaxinger.page-size}")
    private int pageSize;

    public List<ResponseDto> findByUserDynamicQuery(Long userId, LocalDate localDate, TransactionType transactionType, int page){
        return jpaQueryFactory
                .select(Projections.constructor(ResponseDto.class,
                        QBankTransaction.bankTransaction.id,
                        QBankTransaction.bankTransaction.date,
                        QBankTransaction.bankTransaction.account.id,
                        QBankTransaction.bankTransaction.bank.bankName,
                        QBankTransaction.bankTransaction.transactionAmount,
                        QBankTransaction.bankTransaction.transactionType))
                .from(QBankTransaction.bankTransaction)
                .innerJoin(QBankTransaction.bankTransaction.bank, QBank.bank)
                .where(QBankTransaction.bankTransaction.account.id.eq(userId),
                        eqLocalDate(localDate),
                        eqTransactionType(transactionType))
                .limit(pageSize)
                .offset(page*pageSize)
                .fetch();
    }

    public List<ResponseDto> findByBankDynamicQuery(String bankCode, LocalDate localDate, TransactionType transactionType, int page){
        return jpaQueryFactory
                .select(Projections.constructor(ResponseDto.class,
                        QBankTransaction.bankTransaction.id,
                        QBankTransaction.bankTransaction.date,
                        QBankTransaction.bankTransaction.account.id,
                        QBankTransaction.bankTransaction.bank.bankName,
                        QBankTransaction.bankTransaction.transactionAmount,
                        QBankTransaction.bankTransaction.transactionType))
                .from(QBankTransaction.bankTransaction)
                .innerJoin(QBankTransaction.bankTransaction.bank, QBank.bank)
                .where(eqBank(bankCode),
                        eqLocalDate(localDate),
                        eqTransactionType(transactionType))
                .limit(pageSize)
                .offset(page*pageSize)
                .fetch();
    }

    private BooleanExpression eqLocalDate(LocalDate localDate){
        if(null == localDate){
            return null;
        }
        return QBankTransaction.bankTransaction.date.eq(localDate);
    }

    private BooleanExpression eqTransactionType(TransactionType transactionType){
        if(transactionType == null){
            return null;
        }
        return QBankTransaction.bankTransaction.transactionType.eq(transactionType);
    }

    private BooleanExpression eqBank(String bankCode){
        if(bankCode == null){
            return null;
        }
        return QBankTransaction.bankTransaction.bank.id.eq(bankCode);
    }
}
