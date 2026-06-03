PocketProf 📱📈
PocketProf is a modern, student-centric Expense & Budget Management Android application designed to help individuals track their income, budget effectively, and gain real-time analytics on their spending habits. Built natively in Java adhering to the industry-standard MVVM architectural pattern, the application integrates Firebase for user isolation and persistent data cloud management.

✨ Features
Authentication & User Isolation: Secured Signup, Login, and Password Reset utilizing Firebase Authentication. Multi-user database structures ensure each user's financial details remain private and isolated.

Dynamic Transaction Tracking: Effortlessly record both Income and Expenses with localized contextual attributes (Amount, Category, Date, Payment Mode like Cash/Bank/Paytm, and specialized Notes).

Time-Categorized Views: Toggle effortlessly between Daily summaries and Monthly aggregations via a clean Tabbed Layout system.

Data Visualization: Built-in dynamic analytics powered by AnyChart displaying categorized financial health (e.g., Business vs. Salary or Food vs. Travel distribution) via responsive interactive charts.

Profile Management: Fully interactive profile dashboard supporting real-time cloud image updating (Firebase Storage), integrated share intents, and user account management.

Monetization Integration: Ready-to-go deployment scripts with integrated Google AdMob (including banner and full-screen interstitial hooks) for app monetization.

🛠️ Architecture & Tech Stack
Frameworks & Architecture
Language: Java

Architecture: MVVM (Model-View-ViewModel) paired with ViewBinding

Design Guidelines: Material Design 3 featuring clean gradient animations and cards.

Backend & Libraries
Authentication: Firebase Auth

Database: Firebase Cloud Firestore (NoSQL structures configured with composite indexing for query order optimizations)

Cloud Storage: Firebase Storage (profile imagery)

Data Visualization: AnyChart Library

Monetization Engine: Google AdMob SDK

📂 Project Directory Structure
📁 views

📁 activity

SplashActivity.java — MotionLayout animated splash handler

LoginActivity.java — User session validator

SignupActivity.java — Firebase registration controller

ForgetPasswordActivity.java — Password recovery gateway

MainActivity.java — Core base shell for bottom navigation bar interaction

📁 fragment

TransactionFragment.java — Primary feed displaying daily/monthly analytics

StateFragment.java — Data visualization interface using AnyChart

AddTransactionFragment.java — Bottom Sheet Dialog processing financial input entries

📁 viewmodel

MainViewModel.java — Mediates Firestore streams to transactional LiveData

📁 adapters

TransactionAdapter.java — Custom layout wrapper managing RecyclerView data binding

CategoryAdapter.java — Multi-span Grid Layout binder for visual category selections

📁 models

Transaction.java — Object configuration blueprint for expenditures

Category.java — Object properties mapping names, coloring and icon keys

UserModel.java — Object configuration for account properties

📁 util

Constant.java — Hardcoded references ensuring unified query strings

Helper.java — Format string parsing utility functions for timestamps

📁 admob — AdMob abstraction interface layers

🚀 Setup & Installation
1. Clone the repository
Bash
git clone https://github.com/yourusername/PocketProf.git
2. Configure Firebase Integration
Go to the Firebase Console and create a new project.

Add a new Android App to the console using your project package identifier.

Download the generated google-services.json config file and place it inside your app module level directory (/app/google-services.json).

Enable the following operations inside the console:

Authentication: Turn on the Email/Password sign-in provider.

Cloud Firestore: Build an instance matching test mode configurations. Create a composite index mapping transactions using a sorting key tracking type (Ascending) and date (Ascending).

Storage: Activate an accessible instance.

3. Add Google AdMob Credentials (Optional for Testing)
Ensure your system references testing credentials before live releases. In AndroidManifest.xml, configure your metadata tag placeholder matching your application configuration mapping:

XML
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-3940256099942544~3347511713"/> <!-- Your official or test code here -->
📷 UI / Screenshots & Demo Insights
The interface abstracts standard tables into a dynamic experience:

Home Dashboard: Mimics modern credit/debit card layouts grouping global balance analytics above your list transactions.

The Empty-State Engine: Automatically updates the layout with micro-interactions and illustration pointers whenever search results or ledger statements yield null datasets.

Contextual Insights: Evaluates incoming user patterns dynamically across specified categories over different intervals.
