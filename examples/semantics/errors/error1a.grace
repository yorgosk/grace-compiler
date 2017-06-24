fun error1a () : nothing
	var foo : int[10];
	var bar : int[10][10];
{
	foo<-10;
	$bar[0]<-10;
	$bar<-10;
}
