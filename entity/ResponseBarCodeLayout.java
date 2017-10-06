package com.ip.barcodescanner.entity;

/**
 * Created by deepak on 18/8/15.
 */
public class ResponseBarCodeLayout {
    private String result;

    private Details[] details;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Details[] getDetails() {
        return details;
    }

    public void setDetails(Details[] details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "ClassPojo [result = " + result + ", details = " + details + "]";
    }

    public class Details {
        private String delimiter;

        private String gS1Code;

        private String gS1Description;

        private String dataMaxLen;

        private String nonPrintableChar;

        public String getDelimiter() {
            return delimiter;
        }

        public void setDelimiter(String delimiter) {
            this.delimiter = delimiter;
        }

        public String getGS1Code() {
            return gS1Code;
        }

        public void setGS1Code(String gS1Code) {
            this.gS1Code = gS1Code;
        }

        public String getGS1Description() {
            return gS1Description;
        }

        public void setGS1Description(String gS1Description) {
            this.gS1Description = gS1Description;
        }

        public String getDataMaxLen() {
            return dataMaxLen;
        }

        public void setDataMaxLen(String dataMaxLen) {
            this.dataMaxLen = dataMaxLen;
        }

        public String getNonPrintableChar() {
            return nonPrintableChar;
        }

        public void setNonPrintableChar(String nonPrintableChar) {
            this.nonPrintableChar = nonPrintableChar;
        }

        @Override
        public String toString() {
            return "ClassPojo [delimiter = " + delimiter + ", gS1Code = " + gS1Code + ", gS1Description = " + gS1Description + ", dataMaxLen = " + dataMaxLen + ", nonPrintableChar = " + nonPrintableChar + "]";
        }
    }


}