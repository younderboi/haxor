**Full TTY w. Python**

```sh
$ python3 -c 'import pty; pty.spawn("/bin/bash")'
$ export SHELL=bash
$ export TERM=xterm
$ stty rows 38 columns 116
```

```sh
echo export SHELL=bash >> nice_shell.sh
echo export TERM=xterm >> nice_shell.sh
chmod +x niceshell.sh
```

Other way is to deploy an SSH backdoor