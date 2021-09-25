## linux命令

### 1.启用虚拟内存

创建swap文件夹

```
mkdir /usr/swap
```

创建`swapfile`文件(4G)

```
dd if=/dev/zero of=/usr/swap/swapfile bs=1M count=4096
```

查看swap文件

```
du -sh /usr/swap/swapfile
```

将目标设置为swap分区文件

```
mkswap /usr/swap/swapfile
```

激活swap区，并立即启用交换区文件

```
swapon /usr/swap/swapfile
```

设置开机自动启用虚拟内存，在`etc/fstab`文件中加入如下命令

编辑/etc/fstab加入如下内容:

```
/usr/swap/swapfile swap swap defaults 0 0
```

重启reboot