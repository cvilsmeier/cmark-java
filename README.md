# Cmark Java Wrapper

Cmark-wrapper is a java library that converts [Commonmark] 
Markdown to HTML.
Under the covers, cmark-wrapper uses [cmark] 
by John MacFarlane (one of the authors of the commonmark spec) to do its job.


## Usage

With cmark-wrapper, converting md to html is as easy as:

~~~
Wrapper wrapper = new Wrapper();
String md = "# first heading\n## second heading\n";
String html = wrapper.convert(md);
System.out.println(html);
~~~
will print
~~~
<h1>first</h1>
<h2>second</h2>
~~~

The code above asumes that cmark is on your systems search path. 
If it is not, then you have to tell Wrapper() where 
cmark (the executable binary) lives:

~~~
Wrapper wrapper = new Wrapper("C:\\Program Files\\cmark\\cmark.exe");
~~~

## Prerequisites

Cmark must be installed on your system. You can find 
precompiled binaries for Windows and Linux 64bit in this repository, 
see `precompiled/<os_type>` folder. The Windows version is compiled and 
tested under Windows 7. It's a 32bit executable and should run on 
both 64bit and 32bit systems.
The Linux version is compiled and tested on Debian 7 amd64.

For other os types (Linux 32bit, MacOS, Solaris, etc.),
please refer to [cmark] for build instructions.


## Authors

Christoph Vilsmeier is the author of cmark-wrapper.
John MacFarlane is the main author of [cmark].
  
 
[cmark]: https://github.com/jgm/cmark
[Commonmark]: http://commonmark.org

