package com.springboot.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by san6685 on 11/24/2016.
 */

public class OrganisationAbbreviationService{

    private static List<String> countryNames = new ArrayList<>();
    static {
        String [] strings = Locale.getISOCountries();
        for(String  countyCode:strings){
            Locale locale = new Locale("",countyCode);
            countryNames.add(locale.getDisplayCountry());
        }
    }


    private static List<String> commonPrepositions = new ArrayList<>();
    static {
        commonPrepositions.add(" the ");
        commonPrepositions.add(" of");
        commonPrepositions.add(" by ");
        commonPrepositions.add(" for ");
        commonPrepositions.add(" fur ");
        commonPrepositions.add(" at ");
        commonPrepositions.add(" de ");
    }


    public String reduceStringToLessThanFourtyChar(String organisationName){

        String modifiedOrganisationName = organisationName.trim();

        if(modifiedOrganisationName.length()>40){
            modifiedOrganisationName = abbreviateWithOfficialAbbreviations(modifiedOrganisationName);
        }
        if(modifiedOrganisationName.length()>40){
            modifiedOrganisationName = abbreviateCommonTerms(modifiedOrganisationName);
        }
        if(modifiedOrganisationName.length()>40){
            modifiedOrganisationName = removeCountryName(modifiedOrganisationName);
        }
        if(modifiedOrganisationName.length()>40){
            modifiedOrganisationName = removeEmailAddress(modifiedOrganisationName);
        }
        if(modifiedOrganisationName.length()>40){
            modifiedOrganisationName = removeEmailAddress(modifiedOrganisationName);
        }

        modifiedOrganisationName = removeSpecialCharacters(modifiedOrganisationName);

        if(modifiedOrganisationName.length()>40){
            modifiedOrganisationName = removePreposition(modifiedOrganisationName);
        }

        if(modifiedOrganisationName.length()>40){
            modifiedOrganisationName = reduceDepartmentDetails(modifiedOrganisationName);
            modifiedOrganisationName = removeDivisionAtFirst(modifiedOrganisationName);
            modifiedOrganisationName = removeProfessor(modifiedOrganisationName);
        }
        if(modifiedOrganisationName.length()>40){
            modifiedOrganisationName = reduceUniversityDetails(modifiedOrganisationName);
        }

        modifiedOrganisationName = removeAndAtFirst( modifiedOrganisationName);

        modifiedOrganisationName = removeSingleCharAtEnds(modifiedOrganisationName);

        if(modifiedOrganisationName.length()>40){
            modifiedOrganisationName = extractFirstFourWords(modifiedOrganisationName);
        }
        if(modifiedOrganisationName.length()>40){
            modifiedOrganisationName = extractFirstThreeWords(modifiedOrganisationName);
        }
        if(modifiedOrganisationName.length()>40){
            modifiedOrganisationName = trimToFortyChars(modifiedOrganisationName);
        }
        modifiedOrganisationName = modifiedOrganisationName.trim();
        return modifiedOrganisationName;
    }

    private String removeEmailAddress(String organisationName) {
        String modifiedOrganisationName = organisationName;
        Pattern emailPattern = Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = emailPattern.matcher(modifiedOrganisationName);
        if(matcher1.find()){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll(matcher1.group(),"");
        }

        Pattern emailPattern3 = Pattern.compile("\\s{1,}\\w{0,}\\W{0,}\\w{0,}@\\w{0,}\\W{0,}");
        Matcher matcher3 = emailPattern3.matcher(modifiedOrganisationName);
        if(matcher3.find()){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll(matcher3.group(),"");
        }

        return modifiedOrganisationName;
    }
    private String extractFirstFourWords(String organisationName) {
        String modifiedOrganisationName = organisationName;
        Pattern pattern2 = Pattern.compile("(\\w{1,}\\W{0,}\\s?\\W{0,}\\w{0,}\\s?\\W{0,}\\w{0,}\\s?\\W{0,}\\w{0,}\\s?)(.*)");
        Matcher matcher2 = pattern2.matcher(modifiedOrganisationName);
        if(matcher2.find()){
            modifiedOrganisationName =matcher2.group(1);
        }
        return modifiedOrganisationName;
    }
    private String removeSingleCharAtEnds(String organisationName) {
        String modifiedOrganisationName = organisationName;
        Pattern pattern1 = Pattern.compile("\\s+[A-Z&().']$",Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = pattern1.matcher(modifiedOrganisationName);
        if(matcher1.find()){
            modifiedOrganisationName = modifiedOrganisationName.replace(matcher1.group(),"");
        }
        Pattern pattern2 = Pattern.compile("^[A-Z&().']\\s+",Pattern.CASE_INSENSITIVE);
        Matcher matcher2 = pattern2.matcher(modifiedOrganisationName);
        if(matcher2.find()){
            modifiedOrganisationName = modifiedOrganisationName.replace(matcher2.group(),"");
        }
        return modifiedOrganisationName;
    }
    private String extractFirstThreeWords(String organisationName) {
        String modifiedOrganisationName = organisationName;
        Pattern pattern2 = Pattern.compile("(\\w{1,}\\W{0,}\\s?\\W{0,}\\w{0,}\\s?\\W{0,}\\w{0,}\\s?)(.*)");
        Matcher matcher2 = pattern2.matcher(modifiedOrganisationName);
        if(matcher2.find()){
            modifiedOrganisationName =matcher2.group(1);
        }
        return modifiedOrganisationName;
    }
    private String trimToFortyChars(String organisationName) {
        String modifiedOrganisationName = organisationName;
        modifiedOrganisationName = modifiedOrganisationName.replace(modifiedOrganisationName.substring(39),"")+".";
        return modifiedOrganisationName;
    }


    private String abbreviateCommonTerms(String organisationName){
        String modifiedOrganisationName;
        modifiedOrganisationName = removeExtraBlankSpaces(organisationName);
        modifiedOrganisationName = abbreviateUniversity(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateCollege(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateDepartment(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateInstitute(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateFaculty(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateCompany(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateCorporation(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateConsortium(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateHospital(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateAcademy(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateMedicine(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateMathematics(modifiedOrganisationName);
        modifiedOrganisationName = abbreviatePharmacy(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateResearch(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateDevelopment(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateResources(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateSaint(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateBibliothek(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateSociety(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateScience(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateAdministration(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateAgriculture(modifiedOrganisationName);
        modifiedOrganisationName = abbreviatePhysics(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateMedical(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateCentre(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateSchool(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateSecond(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateAffiliate(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateEngineering(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateComputer(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateTechnology(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateInfo(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateBioTech(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateLab(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateEnvironment(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateProfessor(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateInternational(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateNation(modifiedOrganisationName);
        modifiedOrganisationName = removeUSAandUK(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateAssistant(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateAssociate(modifiedOrganisationName);
        modifiedOrganisationName = removeFaxDetails(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateSciences(modifiedOrganisationName);
        modifiedOrganisationName = removeDigits(modifiedOrganisationName);
        return modifiedOrganisationName;
    }

    private String removeFaxDetails(String organisationName) {

        String modifiedOrganisationName = organisationName;

        Pattern pattern1 = Pattern.compile("(?i)Fax:\\d{1,}");
        Matcher faxMatcher1 = pattern1.matcher(modifiedOrganisationName);
        Pattern pattern2 = Pattern.compile("(?i)Fax:\\s[+]\\d{1,}");
        Matcher faxMatcher2 = pattern2.matcher(modifiedOrganisationName);
        Pattern pattern3 = Pattern.compile("(?i)Fax:[+]\\d{1,}");
        Matcher faxMatcher3 = pattern3.matcher(modifiedOrganisationName);
        Pattern pattern4 = Pattern.compile("(?i)Fax:\\s\\d{1,}");
        Matcher faxMatcher4 = pattern4.matcher(modifiedOrganisationName);
        Pattern pattern5 = Pattern.compile("(?i)Fax:\\s\\d{1,}\\s\\d{1,}");
        Matcher faxMatcher5 = pattern5.matcher(modifiedOrganisationName);
        Pattern pattern6 = Pattern.compile("(?i)Fax:[+]\\d{1,}\\s\\d{1,}");
        Matcher faxMatcher6 = pattern6.matcher(modifiedOrganisationName);
        Pattern pattern7 = Pattern.compile("(?i)Fax:\\s[+]\\d{1,}\\s\\d{1,}");
        Matcher faxMatcher7 = pattern7.matcher(modifiedOrganisationName);
        Pattern pattern8 = Pattern.compile("(?i)Fax:");
        Matcher faxMatcher8 = pattern8.matcher(modifiedOrganisationName);
        if(faxMatcher7.find()){
            modifiedOrganisationName = modifiedOrganisationName.replace(faxMatcher7.group(),"");
        }
        if(faxMatcher6.find()){
            modifiedOrganisationName = modifiedOrganisationName.replace(faxMatcher6.group(),"");
        }
        if(faxMatcher5.find()){
            modifiedOrganisationName = modifiedOrganisationName.replace(faxMatcher5.group(),"");
        }

        if(faxMatcher1.find()){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll(faxMatcher1.group(),"") ;
        }
        if(faxMatcher2.find()){
            modifiedOrganisationName = modifiedOrganisationName.replace(faxMatcher2.group(),"");
        }
        if(faxMatcher3.find()){
            modifiedOrganisationName = modifiedOrganisationName.replace(faxMatcher3.group(),"");
        }
        if(faxMatcher4.find()){
            modifiedOrganisationName = modifiedOrganisationName.replace(faxMatcher4.group(),"");
        }
        if(faxMatcher8.find()){
            modifiedOrganisationName = modifiedOrganisationName.replace(faxMatcher8.group(),"");
        }

        return modifiedOrganisationName;
    }

    private String reduceUniversityDetails(String organisationName){
        String modifiedOrganisationName = organisationName;

        Pattern pattern1 = Pattern.compile("\\w{0,}\\s*\\w{0,}\\s*(?i)Univ\\s?\\w{0,}\\s?\\w{0,}");             /* \\w?\\s\\w?\\s\\w?"); */
        Matcher universityMatcher1 = pattern1.matcher(modifiedOrganisationName);
        if(universityMatcher1.find()){
            modifiedOrganisationName = universityMatcher1.group();
        }
        Pattern pattern2 = Pattern.compile("\\w{0,}\\s*\\w{0,}\\s*(?i)Inst\\s?\\w{0,}\\s?\\w{0,}");                /*     \w{0,1}\s(?i)Inst \w{0,1}\s\w{0,1}\s\w{0,1} */
        Matcher universityMatcher2 = pattern2.matcher(modifiedOrganisationName);
        if(universityMatcher2.find()){
            modifiedOrganisationName = universityMatcher2.group();
        }
        return modifiedOrganisationName;
    }

    private String removeDigits(String organisationName){
        String modifiedOrganisationName = organisationName;
        modifiedOrganisationName = modifiedOrganisationName.replaceAll("[0-9]","");
        return modifiedOrganisationName;
    }

    private String removeProfessor(String organisationName){
        String modifiedOrganisationName = organisationName;
        Pattern pattern1 = Pattern.compile("((?i)Asso Prof\\s)(.*)");
        Matcher universityMatcher1 = pattern1.matcher(modifiedOrganisationName);

        Pattern pattern2 = Pattern.compile("((?i)Assi Prof\\s)(.*)");
        Matcher universityMatcher2 = pattern2.matcher(modifiedOrganisationName);

        Pattern pattern3 = Pattern.compile("((?i)Prof\\s)(.*)");
        Matcher universityMatcher3 = pattern3.matcher(modifiedOrganisationName);

        Pattern pattern4 = Pattern.compile("((?i)Prof)(.*)");
        Matcher universityMatcher4 = pattern4.matcher(modifiedOrganisationName);

        if(universityMatcher2.find()){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll(universityMatcher2.group(1),"");
        }
        if(universityMatcher1.find()){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll(universityMatcher1.group(1),"");
        }
        if(universityMatcher3.find()){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll(universityMatcher3.group(1),"");
        }
        if(universityMatcher4.find()){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll(universityMatcher4.group(1),"");
        }
        return modifiedOrganisationName;
    }

    private String reduceDepartmentDetails(String organisationName){
        String modifiedOrganisationName = organisationName;

        Pattern pattern2 = Pattern.compile("\\w{0,}\\s?(?i)Dept\\s?\\w{0,}\\s?\\w{0,}");
        Matcher departmentMatcher2 = pattern2.matcher(modifiedOrganisationName);
        if(departmentMatcher2.find()){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll(departmentMatcher2.group(),"");
        }
        Pattern pattern1 = Pattern.compile("\\w{0,}\\s?(?i)Dept\\s?\\w{0,}\\s?&\\s?\\w{0,}");
        Matcher departmentMatcher1 = pattern1.matcher(modifiedOrganisationName);
        if(departmentMatcher1.find()){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll(departmentMatcher1.group(),"");
        }

        return modifiedOrganisationName;
    }

    private String removeDivisionAtFirst(String organisationName){
        String modifiedOrganisationName = organisationName;

        Pattern pattern2 = Pattern.compile("\\s?Division\\s?\\w{0,}\\s?");
        Matcher departmentMatcher2 = pattern2.matcher(modifiedOrganisationName);
        if(departmentMatcher2.find()){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll(departmentMatcher2.group(),"");
        }

        Pattern pattern1 = Pattern.compile("\\s?Division\\s?\\w{0,}\\s?&\\s?\\w{0,}");
        Matcher departmentMatcher1 = pattern1.matcher(modifiedOrganisationName);

        if(departmentMatcher1.find()){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll(departmentMatcher1.group(),"");
        }
        return modifiedOrganisationName;
    }



    private String removeUSAandUK(String organisationName) {
        String modifiedOrganisationName = organisationName;
        if(modifiedOrganisationName.contains("USA")){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll("USA","");
        }
        if(modifiedOrganisationName.contains("UK")){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll("UK","");
        }
        return modifiedOrganisationName;
    }

    private String removeExtraBlankSpaces(String organisationName) {
        String modifiedOrganisationName = organisationName;
        Pattern pattern = Pattern.compile("\\p{javaWhitespace}{1,}");
        Matcher blankSpacesMatcher = pattern.matcher(modifiedOrganisationName);
        if(blankSpacesMatcher.find()){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll("  ","");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateWithOfficialAbbreviations(String organisationName){
        String modifiedOrganisationName;
        modifiedOrganisationName = abbreviateWHO(organisationName);
        modifiedOrganisationName = abbreviateRAS(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateCAS(modifiedOrganisationName);
        modifiedOrganisationName = abbreviatePAS(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateMPI(modifiedOrganisationName);
        modifiedOrganisationName = abbreviateCHU(modifiedOrganisationName);
        return modifiedOrganisationName;
    }




    private String removeSpecialCharacters(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(modifiedOrganisationName.contains("\"")){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll("\"","");
        }
        if(modifiedOrganisationName.contains("?")){
            modifiedOrganisationName = modifiedOrganisationName.replace("?","");
        }
        if(modifiedOrganisationName.contains("\\")){
            modifiedOrganisationName =modifiedOrganisationName.replace('\\',' ');
        }
        if(modifiedOrganisationName.contains("/")){
            modifiedOrganisationName = modifiedOrganisationName.replace('/',' ');
        }
        if(modifiedOrganisationName.contains("#")){
            modifiedOrganisationName = modifiedOrganisationName.replace("#","");
        }

        if(modifiedOrganisationName.contains("*")){
            modifiedOrganisationName = modifiedOrganisationName.replace("*","");
        }
        if(modifiedOrganisationName.contains("'")){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll("'","");
        }
        if(modifiedOrganisationName.contains("and")){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll("and","&");
        }
        if(modifiedOrganisationName.contains(",")){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll(",","");
        }
        if(modifiedOrganisationName.contains(":")){
            modifiedOrganisationName = modifiedOrganisationName.replace(":","");
        }
        if(modifiedOrganisationName.contains("-")){
            modifiedOrganisationName = modifiedOrganisationName.replace("-","");
        }
        if(modifiedOrganisationName.contains(".")){
            modifiedOrganisationName = modifiedOrganisationName.replace(".","");
        }
        if(modifiedOrganisationName.contains("+")){
            modifiedOrganisationName = modifiedOrganisationName.replace("+","");
        }
        if(modifiedOrganisationName.contains(";")){
            modifiedOrganisationName = modifiedOrganisationName.replace(";","");
        }
        if(modifiedOrganisationName.contains("&;")){
            modifiedOrganisationName = modifiedOrganisationName.replace("&;","");
        }
        if(modifiedOrganisationName.contains("¿½")){
            modifiedOrganisationName = modifiedOrganisationName.replace("¿½","");
        }
        modifiedOrganisationName = modifiedOrganisationName.replaceAll("\\(","");

        modifiedOrganisationName = modifiedOrganisationName.replaceAll("\\)","");

        return modifiedOrganisationName;
    }

    private String removeCountryName(String organisationName){
        String modifiedOrganisationName = organisationName;
        List<String> countyList;
        countyList = countryNames.stream().filter(county -> organisationName.toLowerCase().contains(county.toLowerCase())).collect(Collectors.toList());
        if(!countyList.isEmpty()){
            String countyName = countryNames.stream().filter(county -> organisationName.toLowerCase().contains(county.toLowerCase())).findFirst().get();
            modifiedOrganisationName = organisationName.replaceAll("(?i)"+countyName,"");

        }
        return modifiedOrganisationName;
    }

    private String removePreposition(String organisationName){
        String modifiedOrganisationName = organisationName;
        List<String> commonWordsInOrganisation;
        commonWordsInOrganisation = commonPrepositions.stream().filter(commonWord -> organisationName.toLowerCase().contains(commonWord)).collect(Collectors.toList());
        for (String commonWord : commonWordsInOrganisation){
            modifiedOrganisationName = modifiedOrganisationName.replaceAll("(?i)"+commonWord,"");
        }
        return modifiedOrganisationName;
    }


    private String abbreviateCollege(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("college")) {
            modifiedOrganisationName = organisationName.replaceAll("(?i)College", "Coll");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateDepartment(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("department")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Department","Dept");
        }
        if(organisationName.toLowerCase().contains("deapartment")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Deapartment","Dept");
        }
        if(organisationName.toLowerCase().contains("departement")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Departement","Dept");
        }
        if(organisationName.toLowerCase().contains("departamento de")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Departamento de","Dept");
        }
        if(organisationName.toLowerCase().contains("departeman")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Departeman","Dept");
        }
        if(organisationName.toLowerCase().contains("departm")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Departm","Dept");
        }
        if(organisationName.toLowerCase().contains("departement")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Departement","Dept");
        }
        if(organisationName.toLowerCase().contains("deapertment")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Deapertment","Dept");
        }
        if(organisationName.toLowerCase().contains("deaprtment")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Deaprtment","Dept");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateFaculty(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("faculty")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)Faculty","Fac.");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateCompany(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("company")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Company","Co");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateAssistant(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("assistant")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Assistant","Assi");
        }
        return modifiedOrganisationName;
    }
    private String abbreviateAssociate(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("associate")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Associate","Asso");
        }
        return modifiedOrganisationName;
    }
    private String abbreviateCorporation(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("corporation")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Corporation","Corp");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateConsortium(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("consortium")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Consortium","Cons");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateAcademy(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("academic ")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Academic","Acad");
        }
        if(organisationName.toLowerCase().contains("academy")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Academy","Acad");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateHospital(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("hospital")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Hospital","Hosp");
        }
        if(organisationName.toLowerCase().contains("hospital")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Hospital","Hosp");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateMedicine(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("medicine")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Medicine","Med");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateInternational(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("international")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)International","Int.");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateMathematics(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("mathematics")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Mathematics","Math");
        }
        return modifiedOrganisationName;
    }

    private String abbreviatePharmacy(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("pharmacy")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Pharmacy","Pharm");
        }
        return modifiedOrganisationName;
    }
    private String removeAndAtFirst(String organisationName){
        String modifiedOrganisationName = organisationName.trim();
        if(organisationName.toLowerCase().contains("&") && organisationName.indexOf('&')==0){
            modifiedOrganisationName =   modifiedOrganisationName.substring(1);
        }
        return modifiedOrganisationName.trim();
    }

    private String abbreviateResearch(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("research")) {
            modifiedOrganisationName = organisationName.replaceAll("(?i)Research", "Res");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateDevelopment(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("development")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Development","Dev");
        }
        return modifiedOrganisationName;
    }
    private String abbreviateEnvironment(String organisationName) {
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("environment")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Environment","Env");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateSciences(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("speciality")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Speciality","Spl");
        }
        if(organisationName.toLowerCase().contains("system")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)System","Sys");
        }
        if(organisationName.toLowerCase().contains("knowledge")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Knowledge","Knowl");
        }
        if(organisationName.toLowerCase().contains("nanotechnology")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Nanotechnology","NanoTech");
        }
        if(organisationName.toLowerCase().contains("biochemistry")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Biochemistry","BioChem");
        }
        if(organisationName.toLowerCase().contains("foundation")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Foundation","FDN");
        }
        if(organisationName.toLowerCase().contains("group")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)group","grp");
        }
        if(organisationName.toLowerCase().contains("road")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Road","Rd");
        }
        if(organisationName.toLowerCase().contains("general")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)General","Gen");
        }
        if(organisationName.toLowerCase().contains("geological")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Geological","Geol");
        }
        if(organisationName.toLowerCase().contains("geology")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Geology","Geol");
        }
        if(organisationName.toLowerCase().contains("diseases")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Diseases","dz");
        }
        if(organisationName.toLowerCase().contains("endocrinology")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Endocrinology","Endo");
        }
        if(organisationName.toLowerCase().contains("cardiology")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Cardiology","Cardiol");
        }
        if(organisationName.toLowerCase().contains("radiology")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Radiology","Rad");
        }
        if(organisationName.toLowerCase().contains("psychology")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Psychology","Psy");
        }
        if(organisationName.toLowerCase().contains("urology")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Urology","UROL");
        }
        if(organisationName.toLowerCase().contains("pathology")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Pathology","Pathol");
        }
        if(organisationName.toLowerCase().contains("service")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Service","SVC");
        }
        if(organisationName.toLowerCase().contains("zoology")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Zoology","Zool");
        }
        if(organisationName.toLowerCase().contains("education")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Education","Edu");
        }
        if(organisationName.toLowerCase().contains("educational")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Educational","Edu");
        }
        if(organisationName.toLowerCase().contains("association")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Association","Ass");
        }
        if(organisationName.toLowerCase().contains("mathematical")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Mathematical","Math");
        }
        if(organisationName.toLowerCase().contains("advanced")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Advanced","Adv.");
        }
        if(organisationName.toLowerCase().contains("chemistry")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Chemistry","Chem");
        }
        if(organisationName.toLowerCase().contains("production")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Production","Prod");
        }
        if(organisationName.toLowerCase().contains("laboratories")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Laboratories","Labs");
        }
        if(organisationName.toLowerCase().contains("management")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Management","Mgmt.");
        }
        if(organisationName.toLowerCase().contains("investigation")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Investigation","Inv");
        }
        if(organisationName.toLowerCase().contains("pharmaceutical")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Pharmaceutical","Pharma");
        }
        if(organisationName.toLowerCase().contains("cardiothoracic")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Cardiothoracic","Cardiol");
        }
        if(organisationName.toLowerCase().contains("phone")){
            modifiedOrganisationName =    organisationName.replace("(?i)phone","");
        }
        if(organisationName.toLowerCase().contains("fax:")){
            modifiedOrganisationName =    organisationName.replace("(?i)Fax:","");
        }
        if(organisationName.toLowerCase().contains("mathematik")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Mathematik","Math");
        }
        if(organisationName.toLowerCase().contains("informatik")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Informatik","Info. ");
        }
        if(organisationName.toLowerCase().contains(" und ")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i) und ","&");
        }
        if(organisationName.toLowerCase().contains("biological")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Biological","Bio");
        }
        if(organisationName.toLowerCase().contains("biology")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Biology","Bio");
        }
        if(organisationName.toLowerCase().contains("Bioinformatics")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Bioinformatics","BioInfo");
        }
        if(organisationName.toLowerCase().contains("director")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Director","Dir");
        }
        if(organisationName.toLowerCase().contains("organization")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Organization","Org");
        }
        if(organisationName.toLowerCase().contains("radiology")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Radiology","Radiol");
        }
        if(organisationName.toLowerCase().contains("gastroenterology")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Gastroenterology"," GE");
        }
        if(organisationName.toLowerCase().contains("management")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Management","Mgmt.");
        }
        if(organisationName.toLowerCase().contains("director")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Director","Dir");
        }
        if(organisationName.toLowerCase().contains("communication")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Communications","COMM");
        }
        if(organisationName.toLowerCase().contains("surgical")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Surgical","SX");
        }
        if(organisationName.toLowerCase().contains("surgery")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Surgery","SX");
        }
        if(organisationName.toLowerCase().contains("materials")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Materials","MTLS");
        }
        if(organisationName.toLowerCase().contains("material")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Material","MTLS");
        }
        if(organisationName.toLowerCase().contains("network")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Network","Netw");
        }
        if(organisationName.toLowerCase().contains("cardiovascular")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Cardiovascular","CV");
        }
        if(organisationName.toLowerCase().contains("univires")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Univires","Univ");
        }
        if(organisationName.toLowerCase().contains("universit")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)universit","Univ");
        }
        if(organisationName.toLowerCase().contains("d&partement")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)D&partement","Dept");
        }
        if(organisationName.toLowerCase().contains("consultant")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Consultant","Conslt");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateResources(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("resources")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Resources","Res");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateSaint(String organisationName){
        String modifiedOrganisationName = organisationName;

        if(organisationName.toLowerCase().contains("saint")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Saint","St");
        }
        if(organisationName.toLowerCase().contains("sainte")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Sainte","St");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateBibliothek(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.contains("Bibliothek") || organisationName.contains("Bibliothek".toLowerCase()) || organisationName.contains("Bibliothek".toUpperCase())){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Bibliothek","Bibl");
        }
        if(organisationName.contains("Biblioteka") || organisationName.contains("Biblioteka".toLowerCase()) || organisationName.contains("Biblioteka".toUpperCase())){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Biblioteka","Bibl");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateSociety(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("society")){
            modifiedOrganisationName =    organisationName.replaceAll("(?i)Society","Soc");
        }
        if(organisationName.toLowerCase().contains("societé")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Societé","Soc");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateScience(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("science")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Science","Sci");
        }
        if(organisationName.toLowerCase().contains("sciences")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Sciences","Sci");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateAdministration(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("administration")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Administration","Admin");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateMedical(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("medical")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)Medical","Medi");
        }
        return modifiedOrganisationName;
    }

    private String abbreviatePhysics(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("physics")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)Physics","Phy");
        }
        return modifiedOrganisationName;
    }
    private String abbreviateComputer(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("computer")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)Computer","Comp");
        }
        return modifiedOrganisationName;
    }
    private String abbreviateEngineering(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("engineering")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)Engineering","Engg");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateBioTech(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("biotechnology")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)Biotechnology","BioTech");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateTechnology(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("technology")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)Technology","Tech");
        }
        return modifiedOrganisationName;
    }
    private String abbreviateInfo(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("information")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)Information","Info. ");
        }
        return modifiedOrganisationName;
    }
    private String abbreviateLab(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("laboratory")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)Laboratory","Lab");
        }
        if(organisationName.toLowerCase().contains("laboratoire")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)laboratoire","Lab");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateCentre(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("centre")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)Centre","Cen");
        }
        if(organisationName.toLowerCase().contains("center")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)Center","Cen");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateSchool(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("school")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)School","Sch");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateSecond(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("second")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)Second","2nd");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateNation(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("nation")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)Nation","Ntl.");
        }
        if(organisationName.toLowerCase().contains("national")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)National","Ntl.");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateProfessor(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("professor")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)Professor","Prof.");
        }
        if(organisationName.toLowerCase().contains("proffesor")){
            modifiedOrganisationName = organisationName.replaceAll("(?i)proffesor","Prof.");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateAffiliate(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("affiliate")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Affiliate","Aff");
        }
        if(organisationName.toLowerCase().contains("affiliation")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Affiliation","Aff");
        }
        if(organisationName.toLowerCase().contains("affiliated")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Affiliated","Aff");
        }
        return modifiedOrganisationName;
    }



    private String abbreviateAgriculture(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("agriculture")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Agriculture","Agric");
        }
        if(organisationName.toLowerCase().contains("agricultural")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Agricultural","Agric");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateInstitute(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("institut")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Institut","Inst");
        }
        if(organisationName.toLowerCase().contains("institute")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Institute","Inst");
        }
        if(organisationName.toLowerCase().contains("instytut")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Instytut","Inst");
        }
        if(organisationName.toLowerCase().contains("istituto per")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Istituto per","Inst for");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateUniversity(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("universite")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Universite","Univ");
        }
        if(organisationName.toLowerCase().contains("universitat")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Universitat","Univ");
        }
        if(organisationName.toLowerCase().contains("university")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)University","Univ");
        }
        if(organisationName.toLowerCase().contains("universität")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Universität","Univ");
        }
        if(organisationName.toLowerCase().contains("universiteit")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Universiteit","Univ");
        }
        if(organisationName.toLowerCase().contains("université")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Université","Univ");
        }
        if(organisationName.toLowerCase().contains("universidad")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Universidad","Univ");
        }
        if(organisationName.toLowerCase().contains("università")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Università","Univ");
        }
        if(organisationName.toLowerCase().contains("uniwersytet")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Uniwersytet","Univ");
        }
        if(organisationName.toLowerCase().contains("universita")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)universita","Univ");
        }
        if(organisationName.toLowerCase().contains("universiti")){
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Universiti","Univ");
        }


        return modifiedOrganisationName;
    }

    private String abbreviateWHO(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("world health organization")){
            modifiedOrganisationName =   organisationName.replaceAll("(?i)World Health Organization", "WHO");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateRAS(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("russian academy of science")) {
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Russian Academy of Science", "RAS");
        }
        return modifiedOrganisationName;
    }
    private String abbreviateCAS(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("chinese academy of science")) {
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Chinese Academy of Science", "CAS");
        }
        return modifiedOrganisationName;
    }

    private String abbreviatePAS(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("polish academy of sciences")) {
            modifiedOrganisationName =  organisationName.replaceAll("(?i)Polish Academy of Sciences", "PAS");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateMPI(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("max planck institute")) {
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Max Planck Institute", "MPI");
        }
        return modifiedOrganisationName;
    }

    private String abbreviateCHU(String organisationName){
        String modifiedOrganisationName = organisationName;
        if(organisationName.toLowerCase().contains("centre hospitalier universitaire")) {
            modifiedOrganisationName =   organisationName.replaceAll("(?i)Centre Hospitalier Universitaire", "CHU");
        }
        return modifiedOrganisationName;
    }

//    private void readAndWriteToFiles(){
//        OrganisationAbbreviationService ob = new OrganisationAbbreviationService();
//        String filePath = "D:\\work\\OrganisationNames.csv";
//        int totalOrganisationCount =0;
//        int countOfOrganisationWithMoreThan40=0;
//        PrintWriter modifiedFile = null;
//        PrintWriter failedNamesFile = null;
//        try {
//            modifiedFile = new PrintWriter(new File("D:\\work\\ModifiedOrganisationNames.csv"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            failedNamesFile = new PrintWriter(new File("D:\\work\\FailedOrganisationNames.csv"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        BufferedReader bufferedReader = null;
//
//        String modifiedOrganisationName;
//        String organisationName;
//        try {
//            bufferedReader = new BufferedReader(new FileReader(filePath));
//        } catch (FileNotFoundException e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//        try {
//            while ((organisationName = bufferedReader.readLine())!=null){
//                totalOrganisationCount++;
//                modifiedOrganisationName = ob.reduceStringToLessThanFourtyChar(organisationName);
//
//                modifiedFile.write(modifiedOrganisationName+'\n');
//                if(modifiedOrganisationName.length()>40){
//                    failedNamesFile.write(modifiedOrganisationName+'\n');
//                    countOfOrganisationWithMoreThan40++;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Total Organisation Names: "+totalOrganisationCount);
//        System.out.println("Count of Organisations having length more than 40 char after abbreviation: "+countOfOrganisationWithMoreThan40);
//
//    }
//
//    public static void main(String[] args) {
//
//        OrganisationAbbreviationService ob = new OrganisationAbbreviationService();
//        String string = "\"Institute of Botany and Botanical Garden, Palynology\"";
//        System.out.println(ob.reduceStringToLessThanFourtyChar(string));
//        System.out.println("Modified size:"+ob.reduceStringToLessThanFourtyChar(string).length());
//          ob.readAndWriteToFiles();
//    }


}
