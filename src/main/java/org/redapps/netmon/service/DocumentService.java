package org.redapps.netmon.service;

import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.Document;
import org.redapps.netmon.payload.DocumentResponse;
import org.redapps.netmon.payload.DocumentRequest;
import org.redapps.netmon.payload.PagedResponse;
import org.redapps.netmon.repository.DocumentRepository;
import org.redapps.netmon.repository.CompanyRepository;
import org.redapps.netmon.security.UserPrincipal;
import org.redapps.netmon.util.AppConstants;
import org.redapps.netmon.util.NetmonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Vector;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final CompanyRepository companyRepository;
    private final LogService logService;

    @Autowired
    public DocumentService(DocumentRepository documentRepository, CompanyRepository companyRepository,
                           LogService logService) {
        this.documentRepository = documentRepository;
        this.companyRepository = companyRepository;
        this.logService = logService;
    }

//    public PagedResponse<DocumentResponse> getAllDocuments(UserPrincipal currentUser,
//                                                           int page, int size) {
//        validatePageNumberAndSize(page, size);
//        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
//        Page<Document> docs = documentRepository.findAll(pageable);
//
//        if (docs.getNumberOfElements() == 0) {
//            return new PagedResponse<>(Collections.emptyList(), docs.getNumber(),
//                    docs.getSize(), docs.getTotalElements(), docs.getTotalPages(), docs.isLast());
//        }
//
//        Vector<DocumentResponse> documentResponses = new Vector<>(10);
//
//        DocumentResponse documentResponse;
//        for (Document document : docs) {
//            documentResponse = new DocumentResponse(document.getId(), document.getName(), document.getPath(),
//                    document.getDescription());
//            documentResponses.add(documentResponse);
//        }
//
//        logService.createLog("GET_ALL_DOCUMENTS", currentUser.getUsername(),
//                NetmonStatus.LOG_STATUS.SUCCESS, "", "", "");
//        return new PagedResponse<>(documentResponses, docs.getNumber(),
//                docs.getSize(), docs.getTotalElements(), docs.getTotalPages(), docs.isLast());
//    }

    /**
     * @param currentUser the user id who currently logged in
     * @param companyId the unique company number
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return document response page by page
     */
    public PagedResponse<DocumentResponse> getAllCustomerDocuments(UserPrincipal currentUser,
                                                                   Long companyId,
                                                                   int page, int size) {
        validatePageNumberAndSize(page, size);


        // find all documents by user id
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Document> docs = documentRepository.findAllByCompanyId(companyId, pageable);

        if (docs.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), docs.getNumber(),
                    docs.getSize(), docs.getTotalElements(), docs.getTotalPages(), docs.isLast());
        }

        // store documents into a list
        Vector<DocumentResponse> documentResponses = new Vector<>(10);
        DocumentResponse documentResponse;
        for (Document document : docs) {
            documentResponse = new DocumentResponse(document.getId(), document.getName(), document.getPath(),
                    document.getDescription());
            documentResponses.add(documentResponse);
        }

        logService.createLog("GET_ALL_DOCUMENTS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "", "", "");
        return new PagedResponse<>(documentResponses, docs.getNumber(),
                docs.getSize(), docs.getTotalElements(), docs.getTotalPages(), docs.isLast());
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param documentRequest the document information object
     * @param companyId the unique company number
     * @return document response
     */
    public Document createDocument(UserPrincipal currentUser, DocumentRequest documentRequest,
                                   Long companyId) {

        // create a new document object
        Document document = new Document(documentRequest.getName(), documentRequest.getDescription(),
                companyRepository.getOne(companyId));

        logService.createLog("CREATE_DOCUMENT", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[companyId=" + companyId + "]", documentRequest.toString(), "");
        return documentRepository.save(document);
    }

    /**
     * @param documentId the unique document number
     * @param companyId the unique company number
     * @param currentUser the user id who currently logged in
     * @return document response
     */
    public DocumentResponse getDocumentById(Long documentId, Long companyId, UserPrincipal currentUser) {

        // find the document by document id
        Optional<Document> documentOptional = documentRepository.findByIdAndCompanyId(documentId, companyId);
        if (!documentOptional.isPresent()) {
            logService.createLog("GET_DOCUMENT_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[documentId=" + documentId + "]", "", "The document not found.");
            throw new ResourceNotFoundException("Document", "documentId", documentId);
        }
        Document document = documentOptional.get();

        logService.createLog("GET_DOCUMENT_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[documentId=" + documentId + "]", "", "");

        // create a new response object
        return new DocumentResponse(document.getId(), document.getName(), document.getPath(),
                document.getDescription());
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param documentId the unique document number
     * @param filename the path of a file
     */
    public void setPath(UserPrincipal currentUser, Long documentId, String filename) {
        Optional<Document> documentOptional = documentRepository.findById(documentId);
        if (!documentOptional.isPresent()) {
            logService.createLog("SET_DOCUMENT_PATH", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[documentId=" + documentId + "]", "", "The document not found.");
            throw new ResourceNotFoundException("Document", "id", documentId);
        }
        Document document = documentOptional.get();

        document.setPath(filename);
        logService.createLog("SET_DOCUMENT_PATH", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[documentId=" + documentId + "]", "", "");
        documentRepository.save(document);
    }

//    public PagedResponse<DocumentResponse> getCompanyDocuments(UserPrincipal currentUser, long companyId,
//                                                               int page, int size) {
//        validatePageNumberAndSize(page, size);
//
//        //Retrieve colocations
//        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
//        Page<Document> documentPages = documentRepository.findAllByCompanyId(companyId, pageable);
//
//        if (documentPages.getNumberOfElements() == 0) {
//            return new PagedResponse<>(Collections.emptyList(), documentPages.getNumber(),
//                    documentPages.getSize(), documentPages.getTotalElements(), documentPages.getTotalPages(),
//                    documentPages.isLast());
//        }
//
//        Vector<DocumentResponse> documentResponses = new Vector<>(10);
//
//        DocumentResponse documentResponse;
//        for (Document document : documentPages) {
//                documentResponse = new DocumentResponse(document.getId(), document.getName(),
//                        document.getPath(), document.getDescription());
//
//                documentResponses.add(documentResponse);
//        }
//
//        logService.createLog("GET_ALL_DOCUMENTS", currentUser.getUsername(), "SUCCESS",
//                "companyId: " + companyId);
//
//        return new PagedResponse<>(documentResponses, documentPages.getNumber(),
//                documentPages.getSize(), documentPages.getTotalElements(), documentPages.getTotalPages(),
//                documentPages.isLast());
//    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
