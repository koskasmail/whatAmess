**הדרך המהירה ביותר לטעון (Deploy) קובץ EAR ב‑WebSphere היא דרך ה־Admin Console, שם ניתן להעלות, להגדיר ולהפעיל את האפליקציה ישירות מתוך הדפדפן.**  
השלבים למטה מתאימים לכל גרסאות WebSphere Application Server (WAS) הנפוצות. 

---

# 🚀 איך לטעון EAR ב‑WebSphere (דרך ה־Admin Console)

## 1. היכנס ל־Admin Console
פתח דפדפן וגש לכתובת:
```
http://<server>:9060/ibm/console
```
או ב־HTTPS:
```
https://<server>:9043/ibm/console
```

התחבר עם משתמש בעל הרשאות Admin.

---

## 2. עבור לתפריט התקנת אפליקציות
בצד שמאל:
**Applications → Application Types → WebSphere enterprise applications**

---

## 3. לחץ על **Install**
תראה כפתור:
**Install → Browse → בחר את קובץ ה‑EAR שלך**

לאחר בחירה, לחץ **Next**.

---

## 4. בחר שיטת פריסה
בחר:
- **Fast Path** (ברירת מחדל) – טוב לרוב המקרים  
או  
- **Detailed** – אם אתה צריך להגדיר ClassLoaders, Security Roles, JNDI וכו'.

---

## 5. הגדרות חשובות במהלך ההתקנה
במסך ההגדרות תוכל להגדיר:

### ✔ שם האפליקציה  
### ✔ Target server / cluster  
### ✔ JNDI bindings  
### ✔ Class loader order  
### ✔ Shared libraries (אם נדרש) 

---

## 6. לחץ **Finish**  
WebSphere יתקין את ה‑EAR ויציג הודעת הצלחה.

---

## 7. הפעל את האפליקציה
לאחר ההתקנה:
- סמן את האפליקציה  
- לחץ **Start**

---

# 🧪 טעינת EAR דרך שורת פקודה (wsadmin)
אם אתה מעדיף CLI:

### התקנה:
```sh
wsadmin.sh -c "AdminApp.install('/path/app.ear', '[-appname MyApp -contextroot /myapp -server server1]')"
```

### הפעלה:
```sh
wsadmin.sh -c "AdminControl.invoke(AdminControl.queryNames('type=Application,name=MyApp,*'), 'start')"
```

---

# 📦 איך לייצא EAR שכבר מותקן (אם צריך)
ב־Admin Console:
**Applications → Enterprise Applications → בחר אפליקציה → Export**  


---

# רוצה שאדריך אותך לפי הגרסה המדויקת שלך?
אם תכתוב לי:
- גרסת WebSphere  
- האם זה Standalone או ND Cluster  
- האם אתה מתקין EAR חדש או מחליף גרסה  

אוכל לתת הוראות מדויקות יותר (כולל ClassLoader, JNDI, Security, JDBC ועוד).
