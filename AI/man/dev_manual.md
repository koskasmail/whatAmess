<a name="topage"></a>

# 

### #1 dev_manual

הנה **מדריך קצר, פרקטי וברור** לשימוש ב‑AI בתכנות — כולל דוגמאות, טיפים, סגנונות עבודה ותבניות מוכנות שתוכל להעתיק ולהשתמש בהן מיד.

---

# 📘 Manual: Using AI for Programming  
*(Short, practical, and developer‑friendly)*

---

## 🚀 1. What AI Can Do for You in Programming
AI יכול לעזור לך ב־:

- **Debugging** — למצוא באגים ולהסביר למה הם קורים  
- **Writing code** — יצירת פונקציות, מחלקות, סקריפטים  
- **Explaining code** — פירוק קוד מסובך לשפה פשוטה  
- **Optimizing code** — שיפור ביצועים וקריאות  
- **Learning new technologies** — מסביר ספריות, מסגרות, תבניות  

---

## 🧠 2. How to Ask AI Effectively (The Formula)
כדי לקבל תשובה טובה, השתמש בתבנית:

### **Template: Good AI Prompt for Coding**
```
Goal: מה אתה רוצה להשיג  
Context: קטע קוד / טכנולוגיה / מגבלות  
Constraints: מה אסור / מה חשוב  
Example: אם יש  
Output style: קצר / מפורט / עם הערות / בלי הערות
```

### ✔ Example
```
Goal: Fix this Python loop  
Context: It prints an extra comma at the end  
Constraints: Keep it simple  
Output style: Clean code only
```

---

## 🛠 3. Practical Examples

### **Example A — Debugging**
**Prompt:**
```
My code prints an extra comma at the end. Fix it:
for i in range(30,0,-5):
    print(i, end=", ")
```

**AI Output:**
```python
print(", ".join(str(i) for i in range(30, 0, -5)))
```

---

### **Example B — Writing a Function**
**Prompt:**
```
Write a Python function that returns True if a number is prime.
Keep it simple and readable.
```

**AI Output:**
```python
def is_prime(n):
    if n < 2:
        return False
    for i in range(2, int(n**0.5)+1):
        if n % i == 0:
            return False
    return True
```

---

### **Example C — Explaining Code**
**Prompt:**
```
Explain what this code does in simple terms:
for i in range(1,31):
    if i % 3 == 0:
        product *= i
```

**AI Output:**
> הלולאה עוברת על מספרים 1–30, בודקת מי מתחלק ב‑3, ומכפילה אותם למשתנה product.

---

## 🎨 4. Styles You Can Request
AI יכול לכתוב קוד בסגנונות שונים:

- **Clean code** — מינימלי וברור  
- **Commented code** — עם הסברים  
- **One‑liner** — שורה אחת  
- **Beginner‑friendly** — הסבר פשוט  
- **Advanced** — מושגים מקצועיים  

---

## 💡 5. Tips for Using AI Like a Pro

- **Be specific** — “Fix this bug” פחות טוב מ־“Fix the comma at the end of the output”.  
- **Give context** — שפה, גרסה, קטע קוד.  
- **Ask for alternatives** — “Give me 3 versions”.  
- **Iterate** — “Make it shorter”, “Add comments”, “Optimize it”.  
- **Use AI as a partner, not a crutch** — תן לו לחדד, לא להחליף אותך.

---

## 📦 6. Ready‑to‑Use Prompt Templates

### **Template: Debugging**
```
I have a bug in my code.
Language: ___
Code:
___
Describe the problem and fix it.
Keep the explanation short.
```

### **Template: Writing Code**
```
Write a function that does:
___
Constraints:
___
Output style: ___
```

### **Template: Learning**
```
Teach me how ___ works.
Explain like I'm a beginner.
Give 2 examples.
```

---

אם תרצה, אוכל להכין לך **מדריך ארוך יותר**, או לבנות **תבניות מותאמות לשפת תכנות מסוימת** כמו Python, JavaScript, C#, או כל שפה אחרת.


----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
