function validateDate() {
    var startDate = jQuery("#startDate").val();
    var endDate = jQuery("#endDate").val();

    if (endDate == "") {
        return true;
    }

    var adaptStart = startDate.substring(3, 5) + "/" + startDate.substring(0, 2) + "/" + startDate.substring(6);
    var adaptEnd = endDate.substring(3, 5) + "/" + endDate.substring(0, 2) + "/" + endDate.substring(6);

    if (dates.compare(adaptStart, adaptEnd) > 0) {
        return false;
    }

    return true;
}