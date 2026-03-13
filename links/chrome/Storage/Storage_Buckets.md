
# Storage_Buckets.md

**Short answer:** *Storage Buckets* in Chrome are a **developer feature** that lets websites organize their stored data (like IndexedDB, Cache Storage, etc.) into separate “buckets.” When you clear site data in Chrome, **yes—these buckets are deleted**. As a regular user, you don’t need to use them. As a developer, you *can* use the Storage Buckets API to manage persistent storage more precisely.   [Chrome Developers](https://developer.chrome.com/docs/web-platform/storage-buckets)

---

# 🗂️ What Are Storage Buckets in Chrome?
Storage Buckets are part of a new web standard introduced in **Chrome 122+**. They give websites more control over how their data is stored and how Chrome decides what to delete when space is low.   [Chrome Developers](https://developer.chrome.com/docs/web-platform/storage-buckets)

### Why they exist
- Websites store data using **IndexedDB**, **localStorage**, **Cache Storage**, etc.
- When your device runs low on space, Chrome may delete this data automatically.
- Storage Buckets let sites separate **important** vs. **temporary** data so Chrome can delete the right things first.   [Chrome Developers](https://developer.chrome.com/blog/new-in-chrome-122/)

### How they work
A website can create multiple buckets, for example:
- **Persistent bucket** → important data that should not be deleted  
- **Temporary bucket** → cache or data safe to delete  
- **Media bucket** → large files Chrome can remove first  

Chrome manages each bucket independently.

---

# 🧹 Are Storage Buckets Deleted When You Clear History?
Yes — when you clear **site data** in Chrome (cookies, cached files, hosted app data), Chrome deletes:
- IndexedDB  
- Cache Storage  
- LocalStorage  
- Service workers  
- **Storage Buckets**

They are treated as part of the website’s stored data.

---

# 👤 How Can *You* Use Storage Buckets?
### If you’re a regular Chrome user
You **don’t** use them directly. Chrome handles everything automatically.

### If you’re a developer
You *can* use the Storage Buckets API to:
- Create buckets
- Mark some data as persistent
- Separate temporary vs. important data
- Improve IndexedDB performance (Chrome 126+)   [Chrome Developers](https://developer.chrome.com/blog/maximum-idb-performance-with-storage-buckets)

### Example (developer usage)
```javascript
// Create or access a bucket
const bucket = await navigator.storage.getBucket("myBucket", {
  durability: "persistent"
});

// Use IndexedDB or Cache Storage inside this bucket
```

---

# 🔍 How to View or Delete Storage Buckets (as a user)
You can’t see buckets directly, but you can see the data they contain.

### Option 1 — Chrome settings
1. Open Chrome  
2. Go to **Settings → Privacy and security → Site settings**  
3. Choose **View permissions and data stored across sites**  
4. Select a site → **Clear data**

### Option 2 — Chrome DevTools (for developers)
1. Press **F12**  
2. Open the **Application** tab  
3. Inspect:
   - IndexedDB  
   - Cache Storage  
   - LocalStorage  
   - Service workers  

These are the storage areas that buckets organize internally.

---

# Want help using Storage Buckets in your own project?
If you're building something, I can show you:
- How to create buckets  
- How to store data inside them  
- How to mark data as persistent  
- How to test eviction behavior  

Just tell me what you're working on!
