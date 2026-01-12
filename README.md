<h1 align="center" id="title">eMax Tools</h1>

<p id="description">These tools are developed to support Maximo/MAS ENVs.</p>

<h2>📦 Project <i><b>eSSLMonitor</b></i></h2> 
Screeshot:
<img src="https://github.com/ayoubi1990/emaxtools/blob/main/ssl-monitor/esslmonitor.jpg" alt="project-screenshot" width="900" height="600/">

<h2>📋 Pre-requisites:</h2>

```
Java 8
```
<p>Add your own configuration in resources/godaddy.properties:</p>
<p>First, Add multiple domain to monitor, just you have to add a new property starting ssl.</p>

```
# Domains List to be SSL validated
ssl.er=ermaximo.com
ssl.max=maxmaximo.com
ssl.google=google.com
```
<p>Second, you have to configure your smtp configuration</p>

```
# SMTP Properties
mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
mail.smtp.ssl.trust=smtp.gmail.com
mail.smtp.username=test@gmail.com
mail.smtp.password=epozvmaenxzxznmsubsu
```

<h2>🛠️ Installation Steps:</h2>

<p>1. How to run it manually</p>

```
java -jar build/eSSLMonitor.jar [start/stop]
```

<p>2. Example</p>

```
java -jar build/eSSLMonitor.jar start
```
  
<h2>💻 Built with</h2>

Technologies used in the project:

*   Java
