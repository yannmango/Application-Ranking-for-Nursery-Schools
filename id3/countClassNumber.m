m=12860;
n=9;
%data=cell(m,n);
fid=fopen('/Users/yananchen/Desktop/train.txt','r');
% for i=1:m
%     for j=1:n
%         data{i.j}=fscanf(fid,'%s',[1,1]);
%     end
% end
data=textscan(fid,'%s %s %s %s %s %s %s %s %s','Delimiter',',');
fclose(fid);
str=data{:,n};
class={'not_recom','recommend','very_recom','priority','spec_prior'};
classLen=length(class);
classCount=zeros(1,n);
for i=1:classLen
     for j=1:x
         if strcmp(class{i},class,cmp)
             classCount(i)=classCount(i)+1;
         end
     end
 end
