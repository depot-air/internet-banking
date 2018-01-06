package com.dwidasa.engine.model.view;

/**
 * Created by IntelliJ IDEA.
 * User: IB
 * Date: 4/23/12
 * Time: 10:39 AM
 */
public class AccountSsppView {
    private int page;
    private int row;
    private int ssppRow;
    private String content;
    private boolean againFlag;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSsppRow() {
        //0, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35 };
        //0, 1, 2, 3, 4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28
    	/*
        ssppRow = 0;
        if (row >= 1 && row <= 12) {
            ssppRow = row + 5;
        } else if (row >= 13) {
            ssppRow = row + 7;
        }
        return ssppRow;
        */
    	return row;
    }

    public void setSsppRow(int ssppRow) {
        this.ssppRow = ssppRow;
        //0, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35 };
        //0, 1, 2, 3, 4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28
        /*
        int temp = 0;
        if (ssppRow >= 6 && ssppRow <= 17) {
            temp = ssppRow - 5;
        } else if (row >= 13) {
            temp = ssppRow - 7;
        }
        */
        this.ssppRow = ssppRow;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getAgainFlag() {
        return againFlag;
    }

    public void setAgainFlag(boolean againFlag) {
        this.againFlag = againFlag;
    }
}
