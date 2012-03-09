#!/usr/bin/perl

@todo = @ARGV;
while ($dir = shift @todo) {
	opendir(D, $dir);
	while ($file = readdir D) {
		if (($file ne ".") && ($file ne "..")) {
			$path = "$dir/$file";
			if (-d $path) {
				push(@todo, $path);
			} elsif ((-f $path) && ($file =~ /^(.*)\.java$/)) {
				my $classname = $1;
				rename($path, "$path.bak");
				open(SRC, "$path.bak");
				open(DST, ">$path");
				while (<SRC>) {
					if ($_ =~ s/implements Serializable/implements Serializable, Cloneable/) {
						print DST $_;
						$classdefstarts = 1;
					} elsif (defined $classdefstarts) {
						print DST $_;
						if ($_ =~ /^\{/) {
							print DST "    public $classname clone() throws CloneNotSupportedException {\n";
							print DST "        return ($classname)super.clone();\n";
							print DST "    }\n";
							undef $classdefstarts;
						}
					} else {
						print DST $_;
					}
				}
				close SRC;
				close DST;
				unlink "$path.bak";
			}
		}
	}
	closedir D;
}
