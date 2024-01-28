
# System enumeration

**Kernel dump**
```sh
uname -a
```

**User dump**
```sh
cat /etc/passwd
```

**Process dump**
```sh
$ ps aux
```

**Writeable files**
```sh
$ find / -writable 2>/dev/null | cut -d "/" -f 2,3 | grep -v proc | sort -u
```

**Dump cron tab**
```sh
cat /etc/crontab
```



https://github.com/rebootuser/LinEnum?source=post_page-----973cb62cbe8d--------------------------------