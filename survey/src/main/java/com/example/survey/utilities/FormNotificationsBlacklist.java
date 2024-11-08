// package com.example.survey.utilities;

// import org.springframework.stereotype.Service;

// import java.util.HashSet;
// import java.util.Set;

// @Service
// public class FormNotificationsBlacklist {

//     private final Set<Long> blacklistedFormIds = new HashSet<>();

//     public boolean isBlacklisted(long formId) {
//         return blacklistedFormIds.contains(formId);
//     }

//     public void blacklist(long formId) {
//         blacklistedFormIds.add(formId);
//     }

//     public void removeFromBlacklist(long formId) {
//         blacklistedFormIds.remove(formId);
//     }
// }


// IMPLEMENTED BY QUERYING FOR PRESENT USERS BEFOREHAND -> MORE RELIABLE