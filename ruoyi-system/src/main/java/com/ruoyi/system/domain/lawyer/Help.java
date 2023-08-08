package com.ruoyi.system.domain.lawyer;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName : Help
 * @Description : 帮助中心
 * @Author : WANGKE
 * @Date: 2023-07-12 15:48
 */
public class Help extends BaseEntity {
    @Excel(name = "id", cellType = Excel.ColumnType.NUMERIC)
    private Long id;

    @Excel(name = "问题")
    @NotBlank(message = "问题不能为空")
    private String question;

    @Excel(name = "答案")
    @NotBlank(message = "答案不能为空")
    private String answer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
