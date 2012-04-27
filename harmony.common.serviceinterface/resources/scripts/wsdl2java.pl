#!/bin/perl

my %wsdl;
my %ws;
my %descriptor;
my %packageIn;
my %packageOut;
my %handler;

my $clientFlags;
my $serverFlags;

################################################################################

$serverFlags = $ENV{'SERVER_FLAGS'};
$clientFlags = $ENV{'CLIENT_FLAGS'};

################################################################################

my $name = $ARGV[1];
my $type = $ARGV[0];
my $classType = "class";
my $classComment = "";
my $in;
my $out;
my $package;

################################################################################

#
# Display the script usage
#
sub showUsage {
	my $result = "Usage: $0 (client|server|servlet) (";

	foreach $key (keys(%wsdl)) {
		$result = $result.$key."|";
	}
	
	$result = substr($result, 0, -1).")\n";
	
	print $result;
}

#
# Set elementary vars
#
sub init {
	print "Generating ".$type." ".$name."...\n";
	if (!exists $ENV{'wsdl'}) {
		showUsage();
		die;
	}

	print "WSDL found ...\n";	
	if($type eq "server") {
		$in = "$ENV{'DIR_TEMP'}/wsdl2java/JavaSource/$ENV{'server'}";
		$out = "$ENV{'DIR_SRC'}/$ENV{'server'}";
		
		$package = "" . $ENV{'server'};
		$package =~ s/\//\./g;
		
	} elsif($type eq "servlet") {
		$in = "$ENV{'DIR_TEMP'}/wsdl2java/WebContent/";
		$out = "$ENV{'ws'}";
	} elsif($type eq "client") {
		$in = "$ENV{'DIR_TEMP'}/wsdl2java/src/$ENV{'client'}";
		$out = "$ENV{'DIR_SRC'}/$ENV{'server'}/";
		
		$package = "" . $ENV{'server'};
		$package =~ s/\//\./g;
	} else {
		die "Unsupported type";
	}
	#if (0 != system("/bin/bash ./scripts/checkRuntimeEnv.sh")) {
	#	die "Evironment is missing required tool(s)";
	#}
	chmod(+x, "$ENV{'MUSE_HOME'}/bin/*.sh");
}

#
# Rename Client files
#
sub renameFiles {
	$call = "mv $in/MyService.java $in/I" . ucfirst($name) . "Client.java";	
	system($call);
	
	$call = "mv $in/MyServiceProxy.java $in/" . ucfirst($name) . "Client.java";	
	system($call);
}

#
# copy generated files from $in to $out
#
sub copyFiles {
	system("mkdir -p $out");
     system("mkdir -p $ENV{'DIR_LOG'}");
	if ($type eq "client") {
		renameFiles();
	}

	my $call = "cp -r $in/* $out >> $ENV{'DIR_LOG'}/generator.log";
     print "copying files from \n\t\t$in to \n\t\t $out\n";
	system($call);
}


#
# Generate java files from wsdl
#
sub generateFiles {
	my $call;
	if($type eq "server" || $type eq "servlet") {
		$call = "$ENV{'MUSE_HOME'}/bin/wsdl2java.sh -descriptor $ENV{'WSDL_HOME'}/$ENV{'descriptor'} $serverFlags -overwrite -output $ENV{'DIR_TEMP'}/wsdl2java";
	} elsif ($type eq "client") {
		$call = "$ENV{'MUSE_HOME'}/bin/wsdl2java.sh -descriptor $ENV{'WSDL_HOME'}/$ENV{'descriptor'} $clientFlags -overwrite -output $ENV{'DIR_TEMP'}/wsdl2java";
	} else {
		die "unknown type\n";
	}

	system($call);
}

#
# Add comments to generated files
#
sub generateComments {
	my $context = shift;
	
	$$context =~ s/(abstract|public) (class|interface)/$classComment $1 $2/;  
	$$context =~ s/ *(public|private|protected|) ([a-zA-Z]*) (.*)\(([a-zA-Z]*) (.*)\)(\n){0,1}(.*)throws ([a-zA-Z]*)/    \/\*\*\n     \* $3 Handler.\n     \*\n     \* \@param $5 Request\n     \* \@return $5 Response\n     \* \@throws $8 In case of errors \n     \*\/\n    $1 $2 $3 \($4 $5\)\n            throws $8/g;
}

#
# Import request handler
#
sub organizeImports {
	my $context = shift;
	
	if($type eq "server" && $classType eq "class" && $ENV{'handler'} ne "") {
		$$context =~ s/(import org.w3c.dom.Element;)/$1\nimport org.opennaas.extensions.idb.serviceinterface.RequestHandler;/;
	}
}

#
# Correct package according to $in/$out
#
sub adjustPackage {
	my $context = shift;

	if($in ne $out) {		
		$$context =~ s/package (.*);/package $package;/;		
	}
}

#
# Generate handler call in each function
#
sub generateMethodCall {
	my $context = shift;
	
	if($type eq "server" && $ENV{'handler'} ne "") {
		#my $handlerName = substr($ENV{'handler'}, rindex($ENV{'handler'}, ".")+1);
	
		$$context =~ s/Element ([a-zA-Z]*) \(([a-zA-Z]*) (.*)\)(.*)(\n){0,1}(.*)\{(\n){0,1}(.*)(\n){0,1}(.*)\}/Element $1 \(final $2 $3\) $4 $5 $6 \{\n        return $ENV{'handler'}\.getInstance\(\)\.handle\(\"$1\", $3\);\n    \}/g;
	}
}

#
# Remove // comments
#
sub removeComments {
	my @result;
	
	for($i=0;$i<=$#_;$i++) {
		if($_[$i] =~ m/^ *\/\//) {
			
		} else {
			push(@result, $_[$i]);
		}
	}
	
	return @result;
}

#
#Clean up the code
#
sub trimCode {
	my $context = shift;
	
	# Remove all trailing spaces
	$$context =~ s/ *\n/\n/g;
	# Move braces to previous line
	$$context =~ s/\n *\{/ \{/g;
	# Remove all in between whitespaces
	$$context =~ s/(\S) +(\S)/$1 $2/g;
}

#
# Rename Class
#
sub renameClass {
	my $context = shift;
	
	$newName = ucfirst($name) . "Client";

	$$context =~ s/MyServiceProxy/$newName/g;
	$$context =~ s/MyService/I$newName/g;
}

#
# Parse File, get class type (class or interface) and MUSE comment string
#
sub getClassType {
	my @classDoc;
	my $i;
	
	for($i=0;$i<=$#_;$i++) {
		
		# Get comment string
		if($_[$i] =~ m/^[\/\/]/) {
   			if(length($_[$i]) > 4) {
   				push(@classDoc, " * " . substr($_[$i], 3, length($_[$i])-3));
   			}
   			delete $_[$i];
   		}
		
		# get class type
		if($_[$i] =~ m/(abstract|public) (class|interface)/) {
			my $line = $_[$i];
			$line =~ s/(abstract|public) (final ){0,1}(class|interface)(.*)/$3/g;
			$line =~ s/ //g;
			$line =~ s/\n//g;
			
			$classType = $line;
			$classComment = 
			$comment = "\/\*\*\n \* $name $type.\n \*\n" . join("", @classDoc) . " \*\/\n";

			return $line;
		}
	}
}

#
# MAIN
#

init();
generateFiles();

my $dir = $in;

opendir(DIR, $dir) || die "Cannot chdir to $dir ($!)";
@files = grep(/\.java$/,readdir(DIR));
closedir(DIR);

foreach $file (@files) {
   	open(DAT, $dir."/".$file) || die "Cannot open file $dir/$file: $!";
   	@raw_data=<DAT>;
   	close(DAT);
   
   	getClassType(@raw_data);
   	@raw_data = removeComments(@raw_data);
   
   	my $fileContent = join("", @raw_data);
   
	generateComments(\$fileContent);
   	#organizeImports(\$fileContent);  
   	adjustPackage(\$fileContent);
    	generateMethodCall(\$fileContent);
	trimCode(\$fileContent);

	if ($type eq "client") {
		renameClass(\$fileContent);
	}

   	open(DAT, "+>".$dir."/".$file);
   	print DAT $fileContent;
   	close(DAT);
}

copyFiles();

rmdir($in);

print "Script terminated normaly\n";
