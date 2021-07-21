package it.unisa.casper.adapters;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiJavaFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Map;

public class ICompilationUnitAdapter implements ICompilationUnit {

    public ICompilationUnitAdapter(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    @Override
    public org.eclipse.text.edits.UndoEdit applyTextEdit(org.eclipse.text.edits.TextEdit textEdit, IProgressMonitor iProgressMonitor) throws JavaModelException {
        return null;
    }

    @Override
    public void becomeWorkingCopy(IProblemRequestor iProblemRequestor, IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    @Override
    public void becomeWorkingCopy(IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    @Override
    public void commitWorkingCopy(boolean b, IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    @Override
    public IImportDeclaration createImport(String s, IJavaElement iJavaElement, IProgressMonitor iProgressMonitor) throws JavaModelException {
        return null;
    }

    @Override
    public IImportDeclaration createImport(String s, IJavaElement iJavaElement, int i, IProgressMonitor iProgressMonitor) throws JavaModelException {
        return null;
    }

    @Override
    public IPackageDeclaration createPackageDeclaration(String s, IProgressMonitor iProgressMonitor) throws JavaModelException {
        return null;
    }

    @Override
    public IType createType(String s, IJavaElement iJavaElement, boolean b, IProgressMonitor iProgressMonitor) throws JavaModelException {
        return null;
    }

    @Override
    public void discardWorkingCopy() throws JavaModelException {

    }

    @Override
    public void commit(boolean b, IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public IJavaElement findSharedWorkingCopy(IBufferFactory iBufferFactory) {
        return null;
    }

    @Override
    public IJavaElement getOriginal(IJavaElement iJavaElement) {
        return null;
    }

    @Override
    public IJavaElement getOriginalElement() {
        return null;
    }

    @Override
    public IJavaElement[] findElements(IJavaElement iJavaElement) {
        return new IJavaElement[0];
    }

    @Override
    public ICompilationUnit findWorkingCopy(WorkingCopyOwner workingCopyOwner) {
        return null;
    }

    @Override
    public IType[] getAllTypes() throws JavaModelException {
        return new IType[0];
    }

    @Override
    public IImportDeclaration getImport(String s) {
        return null;
    }

    @Override
    public IImportContainer getImportContainer() {
        return null;
    }

    @Override
    public IImportDeclaration[] getImports() throws JavaModelException {
        PsiJavaFile psiJavaFile = (PsiJavaFile) psiClass.getContainingFile();
        IImportDeclarationAdapter[] importDeclarationAdapters = new IImportDeclarationAdapter[psiJavaFile.getImportList().getImportStatements().length];
        for(int i = 0; i<importDeclarationAdapters.length; i++) {
            importDeclarationAdapters[i] = new IImportDeclarationAdapter(psiJavaFile.getImportList().getImportStatements()[i]);
        }
        return importDeclarationAdapters;
    }

    @Override
    public ICompilationUnit getPrimary() {
        return null;
    }

    @Override
    public WorkingCopyOwner getOwner() {
        return null;
    }

    @Override
    public IPackageDeclaration getPackageDeclaration(String s) {
        return null;
    }

    @Override
    public IPackageDeclaration[] getPackageDeclarations() throws JavaModelException {
        return new IPackageDeclaration[0];
    }

    @Override
    public IType getType(String s) {
        return null;
    }

    @Override
    public IType[] getTypes() throws JavaModelException {
        return new IType[0];
    }

    @Override
    public ICompilationUnit getWorkingCopy(IProgressMonitor iProgressMonitor) throws JavaModelException {
        return null;
    }

    @Override
    public ICompilationUnit getWorkingCopy(WorkingCopyOwner workingCopyOwner, IProblemRequestor iProblemRequestor, IProgressMonitor iProgressMonitor) throws JavaModelException {
        return null;
    }

    @Override
    public boolean hasResourceChanged() {
        return false;
    }

    @Override
    public boolean isWorkingCopy() {
        return false;
    }

    @Override
    public void setOptions(Map<String, String> newOptions) {
        ICompilationUnit.super.setOptions(newOptions);
    }

    @Override
    public Map<String, String> getCustomOptions() {
        return ICompilationUnit.super.getCustomOptions();
    }

    @Override
    public Map<String, String> getOptions(boolean inheritJavaCoreOptions) {
        return ICompilationUnit.super.getOptions(inheritJavaCoreOptions);
    }

    @Override
    public org.eclipse.core.resources.IMarker[] reconcile() throws JavaModelException {
        return new org.eclipse.core.resources.IMarker[0];
    }

    @Override
    public void reconcile(boolean b, IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    @Override
    public CompilationUnit reconcile(int i, boolean b, WorkingCopyOwner workingCopyOwner, IProgressMonitor iProgressMonitor) throws JavaModelException {
        return null;
    }

    @Override
    public CompilationUnit reconcile(int i, boolean b, boolean b1, WorkingCopyOwner workingCopyOwner, IProgressMonitor iProgressMonitor) throws JavaModelException {
        return null;
    }

    @Override
    public CompilationUnit reconcile(int i, int i1, WorkingCopyOwner workingCopyOwner, IProgressMonitor iProgressMonitor) throws JavaModelException {
        return null;
    }

    @Override
    public void restore() throws JavaModelException {

    }

    @Override
    public IType findPrimaryType() {
        return null;
    }

    @Override
    public IModuleDescription getModule() throws JavaModelException {
        return ICompilationUnit.super.getModule();
    }

    @Override
    public IJavaElement getSharedWorkingCopy(IProgressMonitor iProgressMonitor, IBufferFactory iBufferFactory, IProblemRequestor iProblemRequestor) throws JavaModelException {
        return null;
    }

    @Override
    public IJavaElement getWorkingCopy() throws JavaModelException {
        return null;
    }

    @Override
    public IJavaElement getWorkingCopy(IProgressMonitor iProgressMonitor, IBufferFactory iBufferFactory, IProblemRequestor iProblemRequestor) throws JavaModelException {
        return null;
    }

    @Override
    public boolean isBasedOn(org.eclipse.core.resources.IResource iResource) {
        return false;
    }

    @Override
    public IJavaElement getElementAt(int i) throws JavaModelException {
        return null;
    }

    @Override
    public ICompilationUnit getWorkingCopy(WorkingCopyOwner workingCopyOwner, IProgressMonitor iProgressMonitor) throws JavaModelException {
        return null;
    }

    @Override
    public void codeComplete(int i, ICodeCompletionRequestor iCodeCompletionRequestor) throws JavaModelException {

    }

    @Override
    public void codeComplete(int i, ICompletionRequestor iCompletionRequestor) throws JavaModelException {

    }

    @Override
    public void codeComplete(int i, CompletionRequestor completionRequestor) throws JavaModelException {

    }

    @Override
    public void codeComplete(int i, CompletionRequestor completionRequestor, IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    @Override
    public void codeComplete(int i, ICompletionRequestor iCompletionRequestor, WorkingCopyOwner workingCopyOwner) throws JavaModelException {

    }

    @Override
    public void codeComplete(int i, CompletionRequestor completionRequestor, WorkingCopyOwner workingCopyOwner) throws JavaModelException {

    }

    @Override
    public void codeComplete(int i, CompletionRequestor completionRequestor, WorkingCopyOwner workingCopyOwner, IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    @Override
    public IJavaElement[] codeSelect(int i, int i1) throws JavaModelException {
        return new IJavaElement[0];
    }

    @Override
    public IJavaElement[] codeSelect(int i, int i1, WorkingCopyOwner workingCopyOwner) throws JavaModelException {
        return new IJavaElement[0];
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public String getSource() throws JavaModelException {
        return psiClass.getContext().getText();
    }

    @Override
    public ISourceRange getSourceRange() throws JavaModelException {
        return null;
    }

    @Override
    public ISourceRange getNameRange() throws JavaModelException {
        return null;
    }

    @Override
    public IJavaElement getAncestor(int i) {
        return null;
    }

    @Override
    public String getAttachedJavadoc(IProgressMonitor iProgressMonitor) throws JavaModelException {
        return null;
    }

    @Override
    public org.eclipse.core.resources.IResource getCorrespondingResource() throws JavaModelException {
        return null;
    }

    @Override
    public String getElementName() {
        return null;
    }

    @Override
    public int getElementType() {
        return 0;
    }

    @Override
    public String getHandleIdentifier() {
        return null;
    }

    @Override
    public IJavaModel getJavaModel() {
        return null;
    }

    @Override
    public IJavaProject getJavaProject() {
        return null;
    }

    @Override
    public IOpenable getOpenable() {
        return null;
    }

    @Override
    public IJavaElement getParent() {
        return null;
    }

    @Override
    public IPath getPath() {
        return null;
    }

    @Override
    public IJavaElement getPrimaryElement() {
        return null;
    }

    @Override
    public org.eclipse.core.resources.IResource getResource() {
        return null;
    }

    @Override
    public org.eclipse.core.runtime.jobs.ISchedulingRule getSchedulingRule() {
        return null;
    }

    @Override
    public org.eclipse.core.resources.IResource getUnderlyingResource() throws JavaModelException {
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public boolean isStructureKnown() throws JavaModelException {
        return false;
    }

    @Override
    public Object getAdapter(Class aClass) {
        return null;
    }

    @Override
    public void close() throws JavaModelException {

    }

    @Override
    public String findRecommendedLineSeparator() throws JavaModelException {
        return null;
    }

    @Override
    public IBuffer getBuffer() throws JavaModelException {
        return null;
    }

    @Override
    public boolean hasUnsavedChanges() throws JavaModelException {
        return false;
    }

    @Override
    public boolean isConsistent() throws JavaModelException {
        return false;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void makeConsistent(IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    @Override
    public void open(IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    @Override
    public void save(IProgressMonitor iProgressMonitor, boolean b) throws JavaModelException {

    }

    @Override
    public IJavaElement[] getChildren() throws JavaModelException {
        return new IJavaElement[0];
    }

    @Override
    public boolean hasChildren() throws JavaModelException {
        return false;
    }

    @Override
    public void copy(IJavaElement iJavaElement, IJavaElement iJavaElement1, String s, boolean b, IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    @Override
    public void delete(boolean b, IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    @Override
    public void move(IJavaElement iJavaElement, IJavaElement iJavaElement1, String s, boolean b, IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    @Override
    public void rename(String s, boolean b, IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    private PsiClass psiClass;
}
