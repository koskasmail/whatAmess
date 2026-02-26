
# action_listener

כדי להוסיף **אירוע לכל הכפתורים הקיימים** בקוד שלך, בצורה נקייה ומרוכזת, אפשר להשתמש בשתי גישות:

1) להוסיף `ActionListener` לכל כפתור בנפרד  
2) ליצור מאזין אחד משותף לכל הכפתורים, ולזהות לפי `getActionCommand()` איזה כפתור נלחץ

הגישה השנייה היא הכי נקייה — במיוחד כשיש הרבה כפתורים.

---

## ⭐ הפתרון המומלץ: מאזין אחד לכל הכפתורים

הוסף את הקוד הבא אחרי יצירת הכפתורים:

```java
// Create one listener for all buttons
ActionListener buttonListener = new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
            case "Add":
                System.out.println("Add button clicked");
                break;

            case "Delete":
                System.out.println("Delete button clicked");
                break;

            case "Exit":
                System.out.println("Exit button clicked");
                ExitProc();
                break;

            case "Select File":
                System.out.println("Select File button clicked");
                break;

            default:
                System.out.println("Unknown button: " + cmd);
        }
    }
};
```

עכשיו חבר את כל הכפתורים למאזין:

```java
addBtn.addActionListener(buttonListener);
deleteBtn.addActionListener(buttonListener);
exitBtn.addActionListener(buttonListener);

// אם יש כפתור נוסף
// Frm1SelectFile.addActionListener(buttonListener);
```

---

## ⭐ מה זה נותן לך?
- כל הכפתורים משתמשים באותו מאזין  
- אין שכפול קוד  
- כל כפתור מדפיס הודעה מתאימה  
- קל להוסיף כפתורים חדשים בעתיד  

---

## ⭐ אם אתה מעדיף להוסיף לכל כפתור מאזין נפרד
גם אפשר:

```java
addBtn.addActionListener(e -> System.out.println("Add clicked"));
deleteBtn.addActionListener(e -> System.out.println("Delete clicked"));
exitBtn.addActionListener(e -> {
    System.out.println("Exit clicked");
    ExitProc();
});
```

---

כדי להוסיף **ActionListener רק ל־exitBtn** בצורה ברורה ונכונה בתוך הקוד שלך, כל מה שאתה צריך הוא לחבר מאזין שמדפיס הודעה ומפעיל את ExitProc().

החיבור נעשה מיד אחרי יצירת הכפתור.

---

## ✔️ הפתרון המדויק שאתה צריך

הוסף את זה אחרי:

```java
exitBtn = new JButton("Exit");
jpfrm1.add(exitBtn);
```

כך:

```java
exitBtn.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Exit button clicked");
        ExitProc();
    }
});
```

---

## ✔️ גרסה קצרה עם Lambda (אם אתה ב‑Java 8+)

```java
exitBtn.addActionListener(e -> {
    System.out.println("Exit button clicked");
    ExitProc();
});
```

---

## מה זה נותן לך
- הדפסה ל־System.out.println בכל לחיצה  
- קריאה לפונקציית ExitProc שלך  
- קוד נקי וברור  

---



-----
