package it.unisa.casper.adapters;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiPackage;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;

public class PackageAdapter implements IPackageFragment {
    private PsiPackage psiPackage;

    public PackageAdapter(PsiPackage psiPackage) {
        this.psiPackage = psiPackage;
    }

    @Override
    public boolean containsJavaResources() throws JavaModelException {
        return false;
    }

    @Override
    public ICompilationUnit createCompilationUnit(String s, String s1, boolean b, IProgressMonitor iProgressMonitor) throws JavaModelException {
        return null;
    }

    @Override
    public IClassFile getClassFile(String s) {
        return null;
    }

    @Override
    public IOrdinaryClassFile getOrdinaryClassFile(String s) {
        return null;
    }

    @Override
    public IModularClassFile getModularClassFile() {
        return null;
    }

    @Override
    public IClassFile[] getAllClassFiles() throws JavaModelException {
        return new IClassFile[0];
    }

    @Override
    public IClassFile[] getClassFiles() throws JavaModelException {
        return new IClassFile[0];
    }

    @Override
    public IOrdinaryClassFile[] getOrdinaryClassFiles() throws JavaModelException {
        return new IOrdinaryClassFile[0];
    }

    @Override
    public ICompilationUnit getCompilationUnit(String s) {
        return null;
    }

    @Override
    public ICompilationUnit[] getCompilationUnits() throws JavaModelException {
        ICompilationUnitAdapter[] iCompilationUnitAdapters = new ICompilationUnitAdapter[psiPackage.getClasses().length];
        for(int i = 0; i < psiPackage.getClasses().length; i++) {
            iCompilationUnitAdapters[i] = new ICompilationUnitAdapter(psiPackage.getClasses()[i]);
        }
        return iCompilationUnitAdapters;
    }

    @Override
    public ICompilationUnit[] getCompilationUnits(WorkingCopyOwner workingCopyOwner) throws JavaModelException {
        return new ICompilationUnit[0];
    }

    @Override
    public boolean exists() {
        return false;
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
        return psiPackage.getQualifiedName();
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
    public int getKind() throws JavaModelException {
        return 0;
    }

    @Override
    public Object[] getNonJavaResources() throws JavaModelException {
        return new Object[0];
    }

    @Override
    public boolean hasSubpackages() throws JavaModelException {
        return false;
    }

    @Override
    public boolean isDefaultPackage() {
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
}
