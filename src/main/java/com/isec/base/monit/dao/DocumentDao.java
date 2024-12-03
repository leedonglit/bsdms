package com.isec.base.monit.dao;

import com.core.security.database.jdbc.BaseDao;
import com.core.util.Sha256;
import com.isec.base.monit.dto.DocumentDto;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class DocumentDao extends BaseDao<DocumentDto,String> {


    public DocumentDto getDocumentDtoByName(String aca,String docName){
        return this.getObject("SELECT * FROM CAS_BMDMS_DOCUMENT_TAB WHERE ACA_ID = ? AND DOC_TITLE = ? ORDER BY DOC_VERSION DESC LIMIT 1",aca,docName);
    }



    public String saveElectronic(String foreignId,String type,String eContact,String eSign){
        String date = String.valueOf(LocalDate.now());
        String hash = Sha256.getSHA256Str(String.format("%s%s%s%s%s",foreignId,type,date,eContact,eSign));
        this.excute("INSERT INTO CAS_BMDMS_ELECTRONIC_TAB(FOREIGN_ID,ELECTRONIC_TYPE,ELECTRONIC_DATE,ELECTRONIC_CONTRACT,ELECTRONIC_SIGNATURE,DATA_HASH) VALUE(?,?,?,?,?,?)",
                foreignId,type,date,eContact,eSign,hash);
        return hash;
    }

}
